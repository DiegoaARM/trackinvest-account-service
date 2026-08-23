# Kubernetes Documentation - TrackInvest Account Service

## Table of Contents

1. [Architecture](#1-architecture)
2. [File structure](#2-file-structure)
3. [Explanation of each file](#3-explanation-of-each-file)
4. [Prerequisites](#4-prerequisites)
5. [Essential commands](#5-essential-commands)
   - [Build and deployment](#51-build-and-deployment)
   - [Cluster management](#52-cluster-management)
   - [Logs and debugging](#53-logs-and-debugging)
6. [Complete workflow](#6-complete-workflow)
7. [Post-deployment verification](#7-post-deployment-verification)
8. [Tearing down the environment](#8-tearing-down-the-environment)

---

## 1. Architecture

The Kubernetes deployment of the **TrackInvest Account Service** consists of 3 main components that run inside the `trackinvest` namespace:

```
┌───────────────────────────────────────────────────┐
│                   Kubernetes Cluster              │
│                                                   │
│  ┌──────────────┐    ┌──────────────┐             │
│  │   Postgres   │    │   RabbitMQ   │             │
│  │  (Deployment)│    │  (Deployment)│             │
│  │   replicas:1 │    │   replicas:1 │             │
│  └──────┬───────┘    └──────┬───────┘             │
│         │                   │                     │
│  ┌──────┴───────┐    ┌──────┴───────┐             │
│  │ postgres-svc │    │ rabbitmq-svc │             │
│  │  ClusterIP   │    │  ClusterIP   │             │
│  │   port:5432  │    │  5672, 15672 │             │
│  └──────────────┘    └──────────────┘             │
│                                                   │
│  ┌──────────────────────────────────────────┐     │
│  │        Account Service (Deployment)      │     │
│  │              replicas: 2                 │     │
│  │        env: ConfigMap + Secret           │     │
│  └──────────────────┬───────────────────────┘     │
│                     │                             │
│  ┌──────────────────┴───────────────────────┐     │
│  │         account-service-svc              │     │
│  │              ClusterIP                   │     │
│  │              port: 8080                  │     │
│  └──────────────────────────────────────────┘     │
└───────────────────────────────────────────────────┘
```

---

## 2. File structure

```
k8s/
├── namespace.yaml              # Isolated namespace
├── configmap.yaml              # Non-sensitive configuration
├── secret.yaml                 # Credentials and secrets
├── postgres-pvc.yaml           # Persistent storage + PostgreSQL Deployment
├── postgres-service.yaml       # Internal PostgreSQL service
├── rabbitmq-deployment.yaml    # RabbitMQ Deployment
├── rabbitmq-service.yaml       # Internal RabbitMQ service
├── app-deployment.yaml         # Spring Boot application Deployment
├── app-service.yaml            # Internal application service
└── kustomization.yaml          # Kustomize orchestrator (defines order and namespace)
```

---

## 3. Explanation of each file

### 3.1 `namespace.yaml`

Creates a `trackinvest` namespace to isolate all project resources from the rest of the cluster.

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: trackinvest
```

### 3.2 `configmap.yaml`

Stores non-sensitive configuration (common environment variables). It is injected into the `account-service` pod via `envFrom.configMapRef`.

| Variable | Value | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/TrackInvest` | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database user |
| `RABBITMQ_HOST` | `rabbitmq` | RabbitMQ host (K8s service name) |
| `RABBITMQ_PORT` | `5672` | AMQP port |
| `RABBITMQ_USER` | `guest` | RabbitMQ user |

### 3.3 `secret.yaml`

Stores sensitive information (passwords, Cognito credentials). It is injected into the pod via `envFrom.secretRef`.

> ⚠️ **Important:** The values are in `stringData` (plain text) to make development easier. In production, use `data` with base64 values or, even better, an external secret manager (External Secrets Operator, Vault, etc.).

**Included secrets:**

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password |
| `RABBITMQ_PASSWORD` | RabbitMQ password |
| `COGNITO_JWK_SET_URI` | Cognito JWKS URI |
| `COGNITO_ISSUER_URI` | Cognito issuer |
| `COGNITO_CLIENT_ID` | Cognito Client ID |
| `COGNITO_CLIENT_SECRET` | Cognito Client Secret |
| `COGNITO_URI` | Cognito base URI |

### 3.4 `postgres-pvc.yaml`

Defines two resources in a single file (separated by `---`):

**PersistentVolumeClaim (`postgres-pvc`):**
- Requests 1Gi of persistent storage
- Access mode `ReadWriteOnce` (a single node can mount it in read-write mode)
- Mounted at `/var/lib/postgresql` inside the container

**Deployment (`postgres`):**
- Image: `postgres:18-alpine`
- 1 replica
- Environment variables for `POSTGRES_DB`, `POSTGRES_USER` and `POSTGRES_PASSWORD` (obtained from the Secret)
- Healthchecks:
  - **Liveness** (every 10s, delay 15s): `pg_isready -U postgres` — detects whether the process is alive
  - **Readiness** (every 5s, delay 5s): `pg_isready -U postgres` — detects whether it accepts connections
- Mounts the PVC at `/var/lib/postgresql` for data persistence

### 3.5 `postgres-service.yaml`

Service of type `ClusterIP` that exposes PostgreSQL internally on port `5432`. The service name (`postgres`) is what is used as the hostname in the connection URL.

### 3.6 `rabbitmq-deployment.yaml`

**Deployment (`rabbitmq`):**
- Image: `rabbitmq:4-management-alpine` (includes the management UI plugin)
- 1 replica
- Ports: `5672` (AMQP) and `15672` (Management UI)
- Environment variables for `RABBITMQ_DEFAULT_USER` and `RABBITMQ_DEFAULT_PASS` (password from the Secret)
- Healthchecks:
  - **Liveness** (every 15s, delay 20s): `rabbitmq-diagnostics check_port_connectivity`
  - **Readiness** (every 5s, delay 10s): `rabbitmq-diagnostics check_port_connectivity`

### 3.7 `rabbitmq-service.yaml`

Service of type `ClusterIP` with two ports:
- `amqp` (5672) — for application connections
- `management` (15672) — for the web administration console

### 3.8 `app-deployment.yaml`

**Deployment (`account-service`):**
- Image: `trackinvest/account-service:latest`
- 2 replicas (high availability)
- `imagePullPolicy: IfNotPresent` — uses the local image if it exists
- Port `8080`
- Environment variables injected from:
  - `configMapRef: account-service-config` (ConfigMap)
  - `secretRef: account-service-secrets` (Secret)
- Healthchecks:
  - **Liveness** (every 10s, delay 30s): GET `/actuator/health/liveness`
  - **Readiness** (every 5s, delay 15s): GET `/actuator/health/readiness`
- Resources:
  - Requests: 256Mi RAM, 250m CPU
  - Limits: 512Mi RAM, 500m CPU

### 3.9 `app-service.yaml`

Service of type `ClusterIP` that exposes the application's port `8080`. Since it is `ClusterIP`, it is only accessible within the cluster. For external access you would need an `Ingress` or switch to `NodePort`/`LoadBalancer`.

### 3.10 `kustomization.yaml`

Root Kustomize file that orchestrates all resources. It defines:
- `namespace: trackinvest` — applies the namespace to all resources (except `namespace.yaml` itself)
- The list of resources in the desired creation order

When running `kubectl apply -k k8s/`, Kustomize processes and combines all YAML files and sends them to the Kubernetes API in the correct order.

---

## 4. Prerequisites

| Tool | Version | Installation |
|---|---|---|
| Docker | 24+ | [docs.docker.com](https://docs.docker.com/) |
| Kubernetes cluster | 1.27+ | Docker Desktop (Settings > Kubernetes > Enable), Minikube, Kind, etc. |
| kubectl | 1.27+ | `choco install kubernetes-cli` or [docs](https://kubernetes.io/docs/tasks/tools/) |
| Java 21 | 21+ | To compile locally |
| Maven Wrapper | - | Included in the project (`mvnw.cmd`) |

### Verify environment

```powershell
# Verify that kubectl points to the correct cluster
kubectl cluster-info

# Verify available nodes
kubectl get nodes

# Verify current context
kubectl config current-context
```

---

## 5. Essential commands

### 5.1 Build and deployment

```powershell
# 1. Build the project (generates the .jar)
.\mvnw.cmd clean package -DskipTests '-Djacoco.skip=true'

# 2. Build the Docker image locally
docker build -t trackinvest/account-service:latest -f dockerfile .

# 3. Deploy everything to Kubernetes using Kustomize
kubectl apply -k k8s/

# 4. Verify that the pods are running
kubectl get pods -n trackinvest

# 5. View all resources in the namespace
kubectl get all -n trackinvest
```

### 5.2 Cluster management

```powershell
# View all resources in the namespace
kubectl get all -n trackinvest

# View pods with more details (IP, node, etc.)
kubectl get pods -n trackinvest -o wide

# View logs of a specific pod
kubectl logs -n trackinvest -l app=account-service

# View logs in real time (follow)
kubectl logs -n trackinvest -l app=account-service -f

# Scale the application to more replicas
kubectl scale deployment account-service -n trackinvest --replicas=3

# View the status of healthchecks
kubectl describe pod -n trackinvest -l app=account-service

# View namespace events
kubectl get events -n trackinvest --sort-by='.lastTimestamp'
```

### 5.3 Logs and debugging

```powershell
# Interactive terminal inside a pod
kubectl exec -n trackinvest -it <pod-name> -- /bin/sh

# View environment variables inside the pod
kubectl exec -n trackinvest deploy/account-service -- env | sort

# Port forwarding to access the app locally
kubectl port-forward -n trackinters servicio/account-service 8080:8080

# Port forwarding to RabbitMQ Management UI
kubectl port-forward -n trackinvest service/rabbitmq 15672:15672

# Describe a resource (useful for debugging)
kubectl describe deployment account-service -n trackinvest
```

---

## 6. Complete workflow

### First time or after code changes

```powershell
# Step 1: Build
.\mvnw.cmd clean package -DskipTests '-Djacoco.skip=true'

# Step 2: Build Docker image
docker build -t trackinvest/account-service:latest -f dockerfile .

# Step 3: Deploy to Kubernetes
kubectl apply -k k8s/

# Step 4: Verify deployment
kubectl get pods -n trackinvest -w
```

### Only K8s configuration changes (without rebuilding)

```powershell
# If you only changed YAML files (ConfigMap, Secrets, etc.)
kubectl apply -k k8s/

# Force a restart of the pods so they pick up the new config
kubectl rollout restart deployment account-service -n trackinvest
```

### After changing the Docker image

```powershell
# Rebuild the image
docker build -t trackinvest/account-service:latest -f dockerfile .

# Force rollout to use the new image
kubectl rollout restart deployment account-service -n trackinvest

# View rollout progress
kubectl rollout status deployment account-service -n trackinvest
```

---

## 7. Post-deployment verification

```powershell
# 1. All pods must be in Running/Ready state
kubectl get pods -n trackinvest
# NAME                              READY   STATUS    RESTARTS   AGE
# account-service-xxx-xxx           1/1     Running   0          1m
# account-service-yyy-yyy           1/1     Running   0          1m
# postgres-xxx-xxx                  1/1     Running   0          1m
# rabbitmq-xxx-xxx                  1/1     Running   0          1m

# 2. Test the app via port forwarding
kubectl port-forward -n trackinvest service/account-service 8080:8080
# Then in the browser: http://localhost:8080/swagger-ui.html

# 3. Verify RabbitMQ Management
kubectl port-forward -n trackinvest service/rabbitmq 15672:15672
# Then in the browser: http://localhost:15672 (user: guest / password: guest)

# 4. View application logs
kubectl logs -n trackinvest -l app=account-service --tail=50
```

### Verify application health

```powershell
# From another pod inside the cluster
kubectl run -n trackinvest test-pod --image=curlimages/curl --rm -it --restart=Never -- sh
# Inside the pod:
curl -s http://account-service:8080/actuator/health | jq .
```

---

## 8. Tearing down the environment

```powershell
# Delete all resources in the trackinvest namespace
kubectl delete -k k8s/

# Verify they were deleted
kubectl get all -n trackinvest

# If any resource gets stuck (e.g. PVC stuck in Terminating):
kubectl delete pvc -n trackinvest --all
```

> ⚠️ **Warning:** Deleting the namespace removes **all** resources, including PostgreSQL persistent data. If you want to preserve the database, comment out `postgres-pvc.yaml` from `kustomization.yaml` before tearing down.

---

## Additional notes

### External access to the application

The `account-service` service is of type `ClusterIP`, so it is only accessible within the cluster. To expose it externally:

**Option 1 — Port forwarding (development):**
```powershell
kubectl port-forward -n trackinvest service/account-service 8080:8080
```

**Option 2 — NodePort:**
Change `app-service.yaml` to `type: NodePort` and access via `http://<node-ip>:<node-port>`

**Option 3 — Ingress (production):**
Create an `Ingress` resource with a controller such as nginx-ingress or traefik.

### Production recommendations

1. **Secrets:** Use `data` with base64 values at minimum, or better yet a secret manager (External Secrets Operator, Sealed Secrets, Vault)
2. **Resources:** Current limits/requests are for development. Adjust according to expected load
3. **Persistence:** The PVC uses the cluster's default `storageClassName`. In production, define a specific `StorageClass`
4. **Database:** For production, consider PostgreSQL operated by the cluster (CrunchyData, Zalando) or an external service (RDS)
5. **Ingress:** Add an Ingress Controller to route HTTP/HTTPS traffic
6. **HPA:** Configure Horizontal Pod Autoscaler to scale automatically based on CPU/memory
