# Resumen de Implementación - TrackInvest Usuarios y Billeteras

## ✅ Funcionalidades Implementadas

### 1. **Consulta de Perfil Completo**
- **Input Port:** `GetUserProfilePort`
- **UseCase:** `GetUserProfileUseCase`
- **DTO:** `GetUserProfileResponseDTO`
- **Endpoint:** `GET /users/me/profile`
- **Descripción:** Recupera el usuario con todas sus billeteras asociadas
- **Manejo de errores:** Lanza `UserNotFoundException` si el usuario no existe

### 2. **Creación de Billetera Adicional**
- **Input Port:** `CreateWalletPort` (actualizado)
- **UseCase:** `CreateWalletUseCase` (mejorado)
- **DTO:** `CreateWalletRequestDTO`
- **Endpoint:** `POST /wallets`
- **Validaciones:**
  - Nombre de billetera: 3-25 caracteres (regla `NameWalletValidRule`)
  - Unicidad del nombre por usuario
- **Excepciones:**
  - `WalletNameDuplicateException` (sin parámetro)
  - `WalletNameInvalidException` (sin parámetro)

### 3. **Actualización de Nombre de Billetera**
- **Input Port:** `UpdateWalletPort`
- **UseCase:** `UpdateWalletUseCase`
- **DTO:** `UpdateWalletRequestDTO` (solo campo `name`)
- **Endpoint:** `PUT /wallets/{walletId}`
- **Validaciones:**
  - Verifica propiedad del usuario mediante `WalletSecurityService`
  - Valida nombre (3-25 caracteres)
  - Verifica unicidad del nuevo nombre
- **Parámetro:** Requiere `cognitoId` del JWT

### 4. **Actualización de Balance (Depositar/Retirar)**
- **Input Port:** `UpdateWalletBalancePort`
- **UseCase:** `UpdateWalletBalanceUseCase`
- **DTO:** `UpdateWalletBalanceRequestDTO` (campos: `amount`, `isDeposit`)
- **Endpoint:** `PUT /wallets/{walletId}/balance`
- **Validaciones:**
  - Verifica propiedad del usuario
  - Cantidad mayor a cero
  - Para retiros: verifica saldo disponible
- **Excepciones:**
  - `InvalidBalanceException` (cantidad <= 0)
  - `InsufficientBalanceException` (saldo insuficiente para retiro)
- **Lógica:** 
  - Si `isDeposit=true`: suma la cantidad
  - Si `isDeposit=false`: resta la cantidad

### 5. **Eliminación de Billetera**
- **Input Port:** `DeleteWalletPort`
- **UseCase:** `DeleteWalletUseCase`
- **Endpoint:** `DELETE /wallets/{walletId}`
- **Validaciones:**
  - Verifica propiedad del usuario
  - No permite eliminar si es la única billetera
- **Excepción:** `WalletCannotDeleteLastException` (sin parámetro)
- **Parámetro:** Requiere `cognitoId` del JWT

## 🔒 Seguridad - Servicio de Validación Reutilizable

### `WalletSecurityPort` y `WalletSecurityService`
**Ubicación:** `application/usecase/wallet/WalletSecurityService.java`

Valida que el usuario (identificado por `cognitoId`) es el propietario de la billetera:
- Busca la billetera por ID
- Compara `cognitoId` del JWT con el del propietario
- Lanza `WalletUnauthorizedException` si no coinciden

**Uso en:**
- `UpdateWalletUseCase`
- `UpdateWalletBalanceUseCase`
- `DeleteWalletUseCase`

**Ventaja:** Reutilizable y centralizado. Puedes usarlo en otras operaciones si es necesario.

## 📋 Excepciones - Sin Parámetros (Mensaje Predefinido)

### Excepciones de Billetera (business):
1. `WalletNameDuplicateException()` → "Wallet name already exists for this user"
2. `WalletNameInvalidException()` → "Wallet name must be between 3 and 25 characters"
3. `WalletCannotDeleteLastException()` → "Cannot delete the last wallet of a user"
4. `WalletUnauthorizedException()` → "You are not authorized to perform this action on this wallet"
5. `InvalidBalanceException()` → "Balance amount must be greater than zero"
6. `InsufficientBalanceException()` → "Insufficient balance to perform this withdrawal"
7. `WalletNotFoundException()` → "The wallet was not found."

## 🏗️ Arquitectura Hexagonal

### Domain (Puro)
- `WalletDomain`: Métodos `changeName()`, `changeBalance()`, `changeCurrency()`
- `NameWalletValidRule`: Validación de nombre (3-25 caracteres)
- Excepciones en `domain/exception/wallet/business/`

### Application (UseCases & Ports)
- **Input Ports:** `CreateWalletPort`, `UpdateWalletPort`, `UpdateWalletBalancePort`, `DeleteWalletPort`, `GetUserProfilePort`
- **Output Ports:** `WalletRepositoryPort`, `WalletSecurityPort`
- **DTOs:** En `ports/in/dto/`

### Infrastructure (Adapters)
- **WalletJpaAdapter:** Implementa `WalletRepositoryPort`
- **WalletRepository (JPA):** Métodos `existsByNameAndUserId()` para validaciones
- **Controllers:** `WalletController`, `UserController`

## 📝 Endpoints Disponibles

### Usuarios
- `GET /users/me` → Perfil básico del usuario
- `GET /users/me/profile` → Perfil completo con billeteras

### Billeteras
- `POST /wallets` → Crear nueva billetera
- `PUT /wallets/{walletId}` → Actualizar nombre
- `PUT /wallets/{walletId}/balance` → Depositar o retirar
- `DELETE /wallets/{walletId}` → Eliminar billetera

## 🔑 Parámetros JWT
Todos los endpoints protegidos requieren JWT con el claim `sub` (cognitoId)

## ⚙️ Validaciones Implementadas

| Validación | Ubicación | Excepción |
|-----------|-----------|-----------|
| Nombre 3-25 caracteres | `NameWalletValidRule` | `WalletNameInvalidException` |
| Unicidad de nombre/usuario | `CreateWalletUseCase`, `UpdateWalletUseCase` | `WalletNameDuplicateException` |
| Propiedad de billetera | `WalletSecurityService` | `WalletUnauthorizedException` |
| Balance > 0 | `UpdateWalletBalanceUseCase` | `InvalidBalanceException` |
| Saldo disponible (retiro) | `UpdateWalletBalanceUseCase` | `InsufficientBalanceException` |
| No eliminar última billetera | `DeleteWalletUseCase` | `WalletCannotDeleteLastException` |

## ✨ Características Destacadas

✅ **Todas las excepciones sin parámetros** - Mensaje predefinido en constructor
✅ **Seguridad centralizada** - `WalletSecurityService` reutilizable
✅ **Balance editable** - Depositar/Retirar dinero
✅ **No editable currency** - Solo al crear billetera
✅ **Controllers integrados** - Todos los endpoints configurados
✅ **JWT en parámetro** - cognitoId extraído automáticamente del token
✅ **Compilación exitosa** - ✓ BUILD SUCCESS

---

**Próximos pasos opcionales:**
- Implementar auditoría de cambios en billeteras
- Agregar transacciones entre billeteras
- Crear excepciones para usuario (similar a WalletSecurityService)

