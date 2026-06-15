# Documentación Kubernetes - TrackInvest Account Service

## Índice

1. [Arquitectura](#1-arquitectura)
2. [Estructura de archivos](#2-estructura-de-archivos)
3. [Explicación de cada archivo](#3-explicación-de-cada-archivo)
4. [Prerrequisitos](#4-prerrequisitos)
5. [Comandos esenciales](#5-comandos-esenciales)
   - [Build y despliegue](#51-build-y-despliegue)
   - [Gestión del cluster](#52-gestión-del-cluster)
   - [Logs y debugging](#53-logs-y-debugging)
6. [Flujo completo de trabajo](#6-flujo-completo-de-trabajo)
7. [Verificación post-despliegue](#7-verificación-post-despliegue)
8. [Destruir el entorno](#8-destruir-el-entorno)

---

## 1. Arquitectura

El despliegue en Kubernetes del **TrackInvest Account Service** consta de 3 componentes principales que se ejecutan dentro del namespace `trackinvest`:

```
┌─────────────────────────────────────────────────────┐
│                   Kubernetes Cluster                 │
│                                                      │
│  ┌──────────────┐    ┌──────────────┐               │
│  │   Postgres   │    │   RabbitMQ   │               │
│  │  (Deployment)│    │  (Deployment) │               │
│  │   replicas:1 │    │   replicas:1 │               │
│  └──────┬───────┘    └──────┬───────┘               │
│         │                   │                        │
│  ┌──────┴───────┐    ┌──────┴───────┐               │
│  │ postgres-svc │    │ rabbitmq-svc │               │
│  │  ClusterIP   │    │  ClusterIP   │               │
│  │   puerto:5432│    │  5672, 15672 │               │
│  └──────────────┘    └──────────────┘               │
│                                                      │
│  ┌──────────────────────────────────────────┐       │
│  │        Account Service (Deployment)       │       │
│  │              replicas: 2                   │       │
│  │        env: ConfigMap + Secret             │       │
│  └──────────────────┬───────────────────────┘       │
│                     │                                │
│  ┌──────────────────┴───────────────────────┐       │
│  │         account-service-svc              │       │
│  │              ClusterIP                    │       │
│  │              puerto: 8080                 │       │
│  └──────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────┘
```

---

## 2. Estructura de archivos

```
k8s/
├── namespace.yaml              # Namespace aislado
├── configmap.yaml              # Configuración no sensible
├── secret.yaml                 # Credenciales y secrets
├── postgres-pvc.yaml           # Almacenamiento persistente + Deployment de PostgreSQL
├── postgres-service.yaml       # Servicio interno de PostgreSQL
├── rabbitmq-deployment.yaml    # Deployment de RabbitMQ
├── rabbitmq-service.yaml       # Servicio interno de RabbitMQ
├── app-deployment.yaml         # Deployment de la aplicación Spring Boot
├── app-service.yaml            # Servicio interno de la aplicación
└── kustomization.yaml          # Orquestador Kustomize (define el orden y namespace)
```

---

## 3. Explicación de cada archivo

### 3.1 `namespace.yaml`

Crea un namespace `trackinvest` para aislar todos los recursos del proyecto del resto del cluster.

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: trackinvest
```

### 3.2 `configmap.yaml`

Almacena configuración no sensible (variables de entorno comunes). Se inyecta en el pod de `account-service` vía `envFrom.configMapRef`.

| Variable | Valor | Descripción |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/TrackInvest` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuario de base de datos |
| `RABBITMQ_HOST` | `rabbitmq` | Host de RabbitMQ (nombre del servicio K8s) |
| `RABBITMQ_PORT` | `5672` | Puerto AMQP |
| `RABBITMQ_USER` | `guest` | Usuario de RabbitMQ |

### 3.3 `secret.yaml`

Almacena información sensible (contraseñas, credenciales Cognito). Se inyecta en el pod vía `envFrom.secretRef`.

> ⚠️ **Importante:** Los valores están en `stringData` (texto plano) para facilitar el desarrollo. En producción, usa `data` con valores base64 o mejor aún, un secreto manager externo (External Secrets Operator, Vault, etc.).

**Secretos incluidos:**

| Variable | Descripción |
|---|---|
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL |
| `RABBITMQ_PASSWORD` | Contraseña de RabbitMQ |
| `COGNITO_JWK_SET_URI` | JWKS URI de Cognito |
| `COGNITO_ISSUER_URI` | Issuer de Cognito |
| `COGNITO_CLIENT_ID` | Client ID de Cognito |
| `COGNITO_CLIENT_SECRET` | Client Secret de Cognito |
| `COGNITO_URI` | URI base de Cognito |

### 3.4 `postgres-pvc.yaml`

Define dos recursos en un mismo archivo (separados por `---`):

**PersistentVolumeClaim (`postgres-pvc`):**
- Solicita 1Gi de almacenamiento persistente
- Modo de acceso `ReadWriteOnce` (un solo nodo puede montarlo en lectura-escritura)
- Se monta en `/var/lib/postgresql` dentro del contenedor

**Deployment (`postgres`):**
- Imagen: `postgres:18-alpine`
- 1 réplica
- Variables de entorno para `POSTGRES_DB`, `POSTGRES_USER` y `POSTGRES_PASSWORD` (obtenida del Secret)
- Healthchecks:
  - **Liveness** (cada 10s, delay 15s): `pg_isready -U postgres` — detecta si el proceso está vivo
  - **Readiness** (cada 5s, delay 5s): `pg_isready -U postgres` — detecta si acepta conexiones
- Monta el PVC en `/var/lib/postgresql` para persistencia de datos

### 3.5 `postgres-service.yaml`

Service tipo `ClusterIP` que expone PostgreSQL internamente en el puerto `5432`. El nombre del servicio (`postgres`) es el que se usa como hostname en la URL de conexión.

### 3.6 `rabbitmq-deployment.yaml`

**Deployment (`rabbitmq`):**
- Imagen: `rabbitmq:4-management-alpine` (incluye el plugin de management UI)
- 1 réplica
- Puertos: `5672` (AMQP) y `15672` (Management UI)
- Variables de entorno para `RABBITMQ_DEFAULT_USER` y `RABBITMQ_DEFAULT_PASS` (password del Secret)
- Healthchecks:
  - **Liveness** (cada 15s, delay 20s): `rabbitmq-diagnostics check_port_connectivity`
  - **Readiness** (cada 5s, delay 10s): `rabbitmq-diagnostics check_port_connectivity`

### 3.7 `rabbitmq-service.yaml`

Service tipo `ClusterIP` con dos puertos:
- `amqp` (5672) — para conexiones de la aplicación
- `management` (15672) — para la consola web de administración

### 3.8 `app-deployment.yaml`

**Deployment (`account-service`):**
- Imagen: `trackinvest/account-service:latest`
- 2 réplicas (alta disponibilidad)
- `imagePullPolicy: IfNotPresent` — usa la imagen local si existe
- Puerto `8080`
- Variables de entorno inyectadas desde:
  - `configMapRef: account-service-config` (ConfigMap)
  - `secretRef: account-service-secrets` (Secret)
- Healthchecks:
  - **Liveness** (cada 10s, delay 30s): GET `/actuator/health/liveness`
  - **Readiness** (cada 5s, delay 15s): GET `/actuator/health/readiness`
- Recursos:
  - Requests: 256Mi RAM, 250m CPU
  - Limits: 512Mi RAM, 500m CPU

### 3.9 `app-service.yaml`

Service tipo `ClusterIP` que expone el puerto `8080` de la aplicación. Al ser `ClusterIP`, solo es accesible dentro del cluster. Para acceso externo se necesitaría un `Ingress` o cambiar a `NodePort`/`LoadBalancer`.

### 3.10 `kustomization.yaml`

Archivo raíz de Kustomize que orquesta todos los recursos. Define:
- `namespace: trackinvest` — aplica el namespace a todos los recursos (excepto el propio `namespace.yaml`)
- La lista de recursos en el orden de creación deseado

Al ejecutar `kubectl apply -k k8s/`, Kustomize procesa y combina todos los YAMLs y los envía a la API de Kubernetes en el orden correcto.

---

## 4. Prerrequisitos

| Herramienta | Versión | Instalación |
|---|---|---|
| Docker | 24+ | [docs.docker.com](https://docs.docker.com/) |
| Kubernetes cluster | 1.27+ | Docker Desktop (Settings > Kubernetes > Enable), Minikube, Kind, etc. |
| kubectl | 1.27+ | `choco install kubernetes-cli` o [docs](https://kubernetes.io/docs/tasks/tools/) |
| Java 21 | 21+ | Para compilar localmente |
| Maven Wrapper | - | Incluido en el proyecto (`mvnw.cmd`) |

### Verificar entorno

```powershell
# Verificar que kubectl apunta al cluster correcto
kubectl cluster-info

# Verificar nodos disponibles
kubectl get nodes

# Verificar contexto actual
kubectl config current-context
```

---

## 5. Comandos esenciales

### 5.1 Build y despliegue

```powershell
# 1. Compilar el proyecto (genera el .jar)
.\mvnw.cmd clean package -DskipTests '-Djacoco.skip=true'

# 2. Construir la imagen Docker localmente
docker build -t trackinvest/account-service:latest -f dockerfile .

# 3. Desplegar todo en Kubernetes usando Kustomize
kubectl apply -k k8s/

# 4. Verificar que los pods estén corriendo
kubectl get pods -n trackinvest

# 5. Ver todos los recursos del namespace
kubectl get all -n trackinvest
```

### 5.2 Gestión del cluster

```powershell
# Ver todos los recursos del namespace
kubectl get all -n trackinvest

# Ver pods con más detalles (IP, nodo, etc.)
kubectl get pods -n trackinvest -o wide

# Ver logs de un pod específico
kubectl logs -n trackinvest -l app=account-service

# Ver logs en tiempo real (follow)
kubectl logs -n trackinvest -l app=account-service -f

# Escalar la aplicación a más réplicas
kubectl scale deployment account-service -n trackinvest --replicas=3

# Ver el estado de los healthchecks
kubectl describe pod -n trackinvest -l app=account-service

# Ver eventos del namespace
kubectl get events -n trackinvest --sort-by='.lastTimestamp'
```

### 5.3 Logs y debugging

```powershell
# Terminal interactiva dentro de un pod
kubectl exec -n trackinvest -it <pod-name> -- /bin/sh

# Ver variables de entorno dentro del pod
kubectl exec -n trackinvest deploy/account-service -- env | sort

# Port forwarding para acceder a la app localmente
kubectl port-forward -n trackinters servicio/account-service 8080:8080

# Port forwarding a RabbitMQ Management UI
kubectl port-forward -n trackinvest service/rabbitmq 15672:15672

# Describir un recurso (útil para debugging)
kubectl describe deployment account-service -n trackinvest
```

---

## 6. Flujo completo de trabajo

### Primera vez o después de cambios en el código

```powershell
# Paso 1: Compilar
.\mvnw.cmd clean package -DskipTests '-Djacoco.skip=true'

# Paso 2: Construir imagen Docker
docker build -t trackinvest/account-service:latest -f dockerfile .

# Paso 3: Desplegar en Kubernetes
kubectl apply -k k8s/

# Paso 4: Verificar despliegue
kubectl get pods -n trackinvest -w
```

### Solo cambios de configuración K8s (sin recopilar)

```powershell
# Si solo cambiaste YAMLs (ConfigMap, Secrets, etc.)
kubectl apply -k k8s/

# Forzar reinicio de los pods para que tomen la nueva config
kubectl rollout restart deployment account-service -n trackinvest
```

### Después de cambiar la imagen Docker

```powershell
# Re-construir la imagen
docker build -t trackinvest/account-service:latest -f dockerfile .

# Forzar rollout para usar la nueva imagen
kubectl rollout restart deployment account-service -n trackinvest

# Ver el progreso del rollout
kubectl rollout status deployment account-service -n trackinvest
```

---

## 7. Verificación post-despliegue

```powershell
# 1. Todos los pods deben estar en estado Running/Ready
kubectl get pods -n trackinvest
# NAME                              READY   STATUS    RESTARTS   AGE
# account-service-xxx-xxx           1/1     Running   0          1m
# account-service-yyy-yyy           1/1     Running   0          1m
# postgres-xxx-xxx                  1/1     Running   0          1m
# rabbitmq-xxx-xxx                  1/1     Running   0          1m

# 2. Probar la app mediante port forwarding
kubectl port-forward -n trackinvest service/account-service 8080:8080
# Luego en el navegador: http://localhost:8080/swagger-ui.html

# 3. Verificar RabbitMQ Management
kubectl port-forward -n trackinvest service/rabbitmq 15672:15672
# Luego en el navegador: http://localhost:15672 (usuario: guest / password: guest)

# 4. Ver los logs de la aplicación
kubectl logs -n trackinvest -l app=account-service --tail=50
```

### Verificar la salud de la aplicación

```powershell
# Desde otro pod dentro del cluster
kubectl run -n trackinvest test-pod --image=curlimages/curl --rm -it --restart=Never -- sh
# Dentro del pod:
curl -s http://account-service:8080/actuator/health | jq .
```

---

## 8. Destruir el entorno

```powershell
# Eliminar todos los recursos del namespace trackinvest
kubectl delete -k k8s/

# Verificar que se eliminaron
kubectl get all -n trackinvest

# Si algún recurso queda atascado (ej. PVC en Terminating):
kubectl delete pvc -n trackinvest --all
```

> ⚠️ **Advertencia:** Eliminar el namespace elimina **todos** los recursos, incluidos los datos persistentes de PostgreSQL. Si querés preservar la base de datos, comentá `postgres-pvc.yaml` del `kustomization.yaml` antes de destruir.

---

## Notas adicionales

### Acceso externo a la aplicación

El service de `account-service` es tipo `ClusterIP`, por lo que solo es accesible dentro del cluster. Para exponerlo externamente:

**Opción 1 — Port forwarding (desarrollo):**
```powershell
kubectl port-forward -n trackinvest service/account-service 8080:8080
```

**Opción 2 — NodePort:**
Cambiar `app-service.yaml` a `type: NodePort` y acceder via `http://<node-ip>:<node-port>`

**Opción 3 — Ingress (producción):**
Crear un recurso `Ingress` con un controlador como nginx-ingress o traefik.

### Recomendaciones para producción

1. **Secrets:** Usar `data` con valores base64 como mínimo, o mejor un secret manager (External Secrets Operator, Sealed Secrets, Vault)
2. **Recursos:** Los limits/requests actuales son para desarrollo. Ajustar según carga esperada
3. **Persistencia:** El PVC usa `storageClassName` por defecto del cluster. En producción, definir un `StorageClass` específico
4. **Base de datos:** Para producción, considerar PostgreSQL operado por el cluster (CrunchyData, Zalando) o un servicio externo (RDS)
5. **Ingress:** Agregar un Ingress Controller para enrutar tráfico HTTP/HTTPS
6. **HPA:** Configurar Horizontal Pod Autoscaler para escalar automáticamente según CPU/memoria
