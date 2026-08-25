# Implementation Summary - TrackInvest Users and Wallets

## ✅ Implemented Features

### 1. **Full Profile Retrieval**
- **Input Port:** `GetUserProfilePort`
- **UseCase:** `GetUserProfileUseCase`
- **DTO:** `GetUserProfileResponseDTO`
- **Endpoint:** `GET /users/me/profile`
- **Description:** Retrieves the user with all their associated wallets
- **Error handling:** Throws `UserNotFoundException` if the user does not exist

### 2. **Additional Wallet Creation**
- **Input Port:** `CreateWalletPort` (updated)
- **UseCase:** `CreateWalletUseCase` (improved)
- **DTO:** `CreateWalletRequestDTO`
- **Endpoint:** `POST /wallets`
- **Validations:**
  - Wallet name: 3-25 characters (`NameWalletValidRule`)
  - Name uniqueness per user
- **Exceptions:**
  - `WalletNameDuplicateException` (no parameter)
  - `WalletNameInvalidException` (no parameter)

### 3. **Wallet Name Update**
- **Input Port:** `UpdateWalletPort`
- **UseCase:** `UpdateWalletUseCase`
- **DTO:** `UpdateWalletRequestDTO` (only `name` field)
- **Endpoint:** `PUT /wallets/{walletId}`
- **Validations:**
  - Verifies user ownership via `WalletSecurityService`
  - Validates name (3-25 characters)
  - Verifies uniqueness of the new name
- **Parameter:** Requires `cognitoId` from the JWT

### 4. **Balance Update (Deposit/Withdraw)**
- **Input Port:** `UpdateWalletBalancePort`
- **UseCase:** `UpdateWalletBalanceUseCase`
- **DTO:** `UpdateWalletBalanceRequestDTO` (fields: `amount`, `isDeposit`)
- **Endpoint:** `PUT /wallets/{walletId}/balance`
- **Validations:**
  - Verifies user ownership
  - Amount greater than zero
  - For withdrawals: verifies available balance
- **Exceptions:**
  - `InvalidBalanceException` (amount <= 0)
  - `InsufficientBalanceException` (insufficient balance for withdrawal)
- **Logic:** 
  - If `isDeposit=true`: adds the amount
  - If `isDeposit=false`: subtracts the amount

### 5. **Wallet Deletion**
- **Input Port:** `DeleteWalletPort`
- **UseCase:** `DeleteWalletUseCase`
- **Endpoint:** `DELETE /wallets/{walletId}`
- **Validations:**
  - Verifies user ownership
  - Does not allow deletion if it is the only wallet
- **Exception:** `WalletCannotDeleteLastException` (no parameter)
- **Parameter:** Requires `cognitoId` from the JWT

## 🔒 Security - Reusable Validation Service

### `WalletSecurityPort` and `WalletSecurityService`
**Location:** `application/usecase/wallet/WalletSecurityService.java`

Validates that the user (identified by `cognitoId`) is the owner of the wallet:
- Looks up the wallet by ID
- Compares the JWT's `cognitoId` with the owner's
- Throws `WalletUnauthorizedException` if they do not match

**Used in:**
- `UpdateWalletUseCase`
- `UpdateWalletBalanceUseCase`
- `DeleteWalletUseCase`

**Advantage:** Reusable and centralized. You can use it in other operations if needed.

## 📋 Exceptions - No Parameters (Predefined Message)

### Wallet Exceptions (business):
1. `WalletNameDuplicateException()` → "Wallet name already exists for this user"
2. `WalletNameInvalidException()` → "Wallet name must be between 3 and 25 characters"
3. `WalletCannotDeleteLastException()` → "Cannot delete the last wallet of a user"
4. `WalletUnauthorizedException()` → "You are not authorized to perform this action on this wallet"
5. `InvalidBalanceException()` → "Balance amount must be greater than zero"
6. `InsufficientBalanceException()` → "Insufficient balance to perform this withdrawal"
7. `WalletNotFoundException()` → "The wallet was not found."

## 🏗️ Hexagonal Architecture

### Domain (Pure)
- `WalletDomain`: Methods `changeName()`, `changeBalance()`, `changeCurrency()`
- `NameWalletValidRule`: Name validation (3-25 characters)
- Exceptions in `domain/exception/wallet/business/`

### Application (UseCases & Ports)
- **Input Ports:** `CreateWalletPort`, `UpdateWalletPort`, `UpdateWalletBalancePort`, `DeleteWalletPort`, `GetUserProfilePort`
- **Output Ports:** `WalletRepositoryPort`, `WalletSecurityPort`
- **DTOs:** In `ports/in/dto/`

### Infrastructure (Adapters)
- **WalletJpaAdapter:** Implements `WalletRepositoryPort`
- **WalletRepository (JPA):** Methods `existsByNameAndUserId()` for validations
- **Controllers:** `WalletController`, `UserController`

## 📝 Available Endpoints

### Users
- `GET /users/me` → Basic user profile
- `GET /users/me/profile` → Full profile with wallets

### Wallets
- `POST /wallets` → Create new wallet
- `PUT /wallets/{walletId}` → Update name
- `PUT /wallets/{walletId}/balance` → Deposit or withdraw
- `DELETE /wallets/{walletId}` → Delete wallet

## 🔑 JWT Parameters
All protected endpoints require a JWT with the `sub` claim (cognitoId)

## ⚙️ Implemented Validations

| Validation | Location | Exception |
|-----------|-----------|-----------|
| Name 3-25 characters | `NameWalletValidRule` | `WalletNameInvalidException` |
| Name/user uniqueness | `CreateWalletUseCase`, `UpdateWalletUseCase` | `WalletNameDuplicateException` |
| Wallet ownership | `WalletSecurityService` | `WalletUnauthorizedException` |
| Balance > 0 | `UpdateWalletBalanceUseCase` | `InvalidBalanceException` |
| Available balance (withdrawal) | `UpdateWalletBalanceUseCase` | `InsufficientBalanceException` |
| Cannot delete last wallet | `DeleteWalletUseCase` | `WalletCannotDeleteLastException` |

## ✨ Highlights

✅ **All exceptions without parameters** - Predefined message in constructor
✅ **Centralized security** - Reusable `WalletSecurityService`
✅ **Editable balance** - Deposit/Withdraw money
✅ **Currency not editable** - Only when creating the wallet
✅ **Integrated controllers** - All endpoints configured
✅ **JWT as parameter** - cognitoId automatically extracted from the token
✅ **Successful build** - ✓ BUILD SUCCESS

---

**Optional next steps:**
- Implement change auditing for wallets
- Add transactions between wallets
- Create user exceptions (similar to WalletSecurityService)
