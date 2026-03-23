# 📈 TrackInvest - Backend API

**TrackInvest** es el núcleo de una plataforma de gestión de inversiones diseñada bajo los más altos estándares de ingeniería de software. El objetivo del proyecto es proporcionar una API escalable, segura y fácil de mantener para el seguimiento de activos financieros.

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21 (LTS)
* **Framework:** Spring Boot 4.0.1
* **Arquitectura:** Hexagonal (Clean Architecture)
* **Seguridad:** Spring Security + OAuth2 (AWS Cognito)
* **Persistencia:** PostgreSQL + Spring Data JPA
* **Documentación:** OpenAPI 3 (Swagger UI)
* **Herramientas:** Lombok, MapStruct, Maven

## 🏗️ Arquitectura del Proyecto

El proyecto se divide en tres capas principales siguiendo el patrón de **Puertos y Adaptadores**:

1.  **Domain:** El corazón del negocio. Contiene las entidades (`UserDomain`), Value Objects (`Name`, `Email`) y las reglas que rigen el sistema, sin dependencias externas.
2.  **Application:** Contiene los Casos de Uso (`SyncUser`, `GetMe`) y define los puertos (Interfaces) para la comunicación entre capas.
3.  **Infrastructure:** Adaptadores de entrada (REST Controllers) y de salida (Persistence con JPA, Cognito Adapters).

## 🚀 Características Principales

- **Identidad Centralizada:** Sincronización automática de usuarios desde AWS Cognito a base de datos local.
- **Seguridad Robusta:** Validación de tokens JWT y protección de endpoints por scopes.
- **Validación de Dominio:** Implementación de excepciones personalizadas (`NombreInvalidoException`) para asegurar la integridad de los datos.
- **Documentación Interactiva:** Swagger UI integrado para pruebas rápidas de los endpoints.

## ⚙️ Configuración Rápida

1. Clonar el repositorio.
2. Configurar las variables de entorno para PostgreSQL y AWS Cognito en `application.properties`.
3. Ejecutar `./mvnw spring-boot:run`.
4. Acceder a Swagger en: `http://localhost:8080/swagger-ui/index.html`.