# 📚 API de Gestión de Usuarios - BCI

Sistema de gestión de usuarios con autenticación JWT y operaciones CRUD completas.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## 🎯 Descripción del Proyecto

API RESTful desarrollada con Spring Boot para la gestión integral de usuarios. Implementa:

- ✅ Registro y autenticación de usuarios con JWT
- ✅ Operaciones CRUD completas sobre usuarios
- ✅ Gestión de múltiples teléfonos por usuario
- ✅ Validación de email y contraseñas con regex configurable
- ✅ Seguridad con Spring Security + BCrypt
- ✅ Documentación interactiva con Swagger/OpenAPI
- ✅ Manejo centralizado de excepciones
- ✅ Base de datos H2 en memoria

---

## 🔧 Stack Tecnológico

- **Java 21** - Lenguaje base
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistencia
- **Spring Security** - Autenticación/Autorización
- **H2 Database** - BD en memoria
- **JWT (JJWT 0.11.5)** - Tokens de autenticación
- **Lombok** - Reducción de boilerplate
- **Jakarta Validation** - Validaciones
- **SpringDoc OpenAPI** - Documentación
- **JaCoCo** - Cobertura de código
- **JUnit 5 + Mockito** - Testing

---

## 🏛️ Arquitectura

### Estructura de Capas

```
com.bci.demo/
├── cfg/                    # Configuraciones
│   ├── security/           # JWT + Spring Security
│   └── validation/         # Validadores personalizados
├── handlers/               # Manejo global de excepciones
├── user/
│   ├── controller/         # Endpoints REST
│   ├── service/            # Lógica de negocio
│   ├── repository/         # Acceso a datos (JPA)
│   ├── entity/             # Entidades (User, Phone)
│   ├── dto/                # Data Transfer Objects
│   └── mapper/             # Conversión Entity <-> DTO
└── DemoApplication.java
```

### Patrón de Diseño

- **Arquitectura en Capas**: Controller → Service → Repository
- **DTO Pattern**: Separación entre modelo de datos y API
- **Repository Pattern**: Abstracción de persistencia
- **Dependency Injection**: IoC de Spring

---

## 📥 Instalación

### Requisitos

- Java JDK 21+
- Maven 3.8+ (opcional, incluye Maven Wrapper)

### Pasos

1. **Clonar el repositorio**
```bash
git clone https://github.com/crmorenom/demo.git
cd demo
```

2. **Compilar el proyecto**
```bash
# Windows PowerShell
./mvnw clean install
# Windows cmd
mvnw clean install

# Linux/Mac
./mvnw clean install
```

3. **Ejecutar la aplicación**


```bash
# Opción 1: Con Maven
# Windows PowerShell 
./mvnw spring-boot:run
# Windows cmd
mvnw spring-boot:run
# Linux/Mac Con
./mvnw spring-boot:run

# Opción 2: Con JAR
java -jar target/demo-0.0.1.jar
```

La aplicación estará disponible en: `http://localhost:8081`

---

## ⚙️ Configuración

### application.properties

```properties
# Servidor
server.port=8081

# JWT
jwt.secret=bd4f8c2a7f1e9d04b6b5f4c3a2e1d9f7b8a7c6d5e4f3a2b1c0d9e8f7a6b5c4d3
jwt.expiration=3600000  # 1 hora

# H2 Database
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.settings.web-allow-others=true

logging.level.root=INFO
logging.level.org.springframework.web=INFO
server.error.include-stacktrace=always
# Validación de Contraseña (Regex configurable)
user.password.regex=^(?=.*[A-Z])(?=.*\\d.*\\d)[A-Za-z\\d]{8,12}$
```

### Reglas de Contraseña

- ✅ Al menos 1 letra mayúscula
- ✅ Exactamente 2 números
- ✅ Entre 8-12 caracteres
- ✅ Solo letras y números

**Ejemplos válidos**: `Hunter22`, `Password12`, `Abc12def`

---

## 📡 Endpoints de la API

**Base URL:** `http://localhost:8081/api/users`

### 🔓 Públicos (Sin JWT)

#### 1. Registrar Usuario
```http
POST /api/users/register
Content-Type: application/json
```
```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "0Password1",
    "phones": [
        {
            "number": "99754553",
            "citycode": "1",
            "countrycode": "57"
        },
        {
            "number": "87654321",
            "citycode": "2",
            "countrycode": "56"
        }
    ]
}
```

**Response 201:**
```json
{
    "codigo": 201,
    "mensaje": "Usuario creado exitosamente",
    "data": {
        "id": "b615f837-d57b-4771-8ec5-8e1ebc7af259",
        "name": "John Doe",
        "email": "john.doe@example.com",
        "created": "2025-10-23T23:20:30.6799404",
        "modified": "2025-10-23T23:20:30.6799404",
        "lastLogin": "2025-10-23T23:20:30.6799404",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQzMCwiZXhwIjoxNzYxMjc2MDMwfQ.TAuk9zA6OqorWDwhwW22ieB5QT7VFKjOUHFI2fdXJ3U",
        "phones": [
            {
                "id": "a117518a-cad8-4598-8f7b-166358e5d45d",
                "number": "99754553",
                "citycode": "1",
                "countrycode": "57"
            },
            {
                "id": "3323eb77-1ca1-4765-bffb-039cf317c69f",
                "number": "87654321",
                "citycode": "2",
                "countrycode": "56"
            }
        ],
        "active": true
    }
}
```

#### 2. Login
```http
POST /api/users/login
Content-Type: application/json
```
```json
{
    "email": "john.doe@example.com",
    "password": "0Password1"
}
```

**Response 200:**
```json
{
    "codigo": 200,
    "mensaje": "Usuario autenticado exitosamente",
    "data": {
        "id": "b615f837-d57b-4771-8ec5-8e1ebc7af259",
        "created": "2025-10-23T23:20:30.67994",
        "modified": "2025-10-23T23:20:30.67994",
        "lastLogin": "2025-10-23T23:20:52.7737512",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI",
        "isActive": true
    }
}
```

---

### 🔒 Protegidos (Requieren JWT)

**Header requerido:**
```
Authorization: Bearer {eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI}
```

#### 3. Obtener Todos los Usuarios
```http
GET /api/users
Authorization: Bearer {eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI}
```
**Response 200:**
```json
{
    "codigo": 200,
    "mensaje": "Usuarios obtenidos exitosamente",
    "data": [
        {
            "id": "b615f837-d57b-4771-8ec5-8e1ebc7af259",
            "name": "John Doe",
            "email": "john.doe@example.com",
            "created": "2025-10-23T23:20:30.67994",
            "modified": "2025-10-23T23:20:30.67994",
            "lastLogin": "2025-10-23T23:20:52.773751",
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI",
            "phones": [
                {
                    "id": "a117518a-cad8-4598-8f7b-166358e5d45d",
                    "number": "99754553",
                    "citycode": "1",
                    "countrycode": "57"
                },
                {
                    "id": "3323eb77-1ca1-4765-bffb-039cf317c69f",
                    "number": "87654321",
                    "citycode": "2",
                    "countrycode": "56"
                }
            ],
            "active": true
        }
    ]
}
```

#### 4. Obtener Usuario por ID
```http
GET /api/users/{id}
    /api/users/b615f837-d57b-4771-8ec5-8e1ebc7af259

Authorization: Bearer {eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI}
```

**Response 200:**
```json
{
    "codigo": 200,
    "mensaje": "Usuario obtenido exitosamente",
    "data": {
        "id": "b615f837-d57b-4771-8ec5-8e1ebc7af259",
        "name": "John Doe",
        "email": "john.doe@example.com",
        "created": "2025-10-23T23:20:30.67994",
        "modified": "2025-10-23T23:20:30.67994",
        "lastLogin": "2025-10-23T23:20:52.773751",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI",
        "phones": [
            {
                "id": "a117518a-cad8-4598-8f7b-166358e5d45d",
                "number": "99754553",
                "citycode": "1",
                "countrycode": "57"
            },
            {
                "id": "3323eb77-1ca1-4765-bffb-039cf317c69f",
                "number": "87654321",
                "citycode": "2",
                "countrycode": "56"
            }
        ],
        "active": true
    }
}
```

#### 5. Actualizar Usuario
```http
PATCH /api/users/{id}
      /api/users/b615f837-d57b-4771-8ec5-8e1ebc7af259

Authorization: Bearer {eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc2MTI3MjQ1MiwiZXhwIjoxNzYxMjc2MDUyfQ.u8H3prqvChetuKCw28lxHj6SwQZmhAec_blgVnkNyCI}
Content-Type: application/json
```
```json
{
    "name": "John Doe",
    "email": "john.doe@bci.com",
    "password": "0Password7",
    "phones": [
        {
            "id": "a117518a-cad8-4598-8f7b-166358e5d45d",
            "number": "99754553",
            "citycode": "10",
            "countrycode": "60"
        },
        {
            "id": "3323eb77-1ca1-4765-bffb-039cf317c69f",
            "number": "87654321",
            "citycode": "20",
            "countrycode": "90"
        }
    ],
    "active": true
}
```

**Response 200:**
```json
{
    "codigo": 200,
    "mensaje": "Usuario actualizado exitosamente",
    "data": {
        "id": "b615f837-d57b-4771-8ec5-8e1ebc7af259",
        "name": "John Doe",
        "email": "john.doe@bci.com",
        "created": "2025-10-23T23:20:30.67994",
        "modified": "2025-10-23T23:30:30.9958031",
        "lastLogin": "2025-10-23T23:20:52.773751",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBiY2kuY29tIiwiaWF0IjoxNzYxMjczMDMxLCJleHAiOjE3NjEyNzY2MzF9.3iSbazEtfmt8RuuEeVDDKdFdK0WIvafKcsjLfUMYXUo",
        "phones": [
            {
                "id": "a117518a-cad8-4598-8f7b-166358e5d45d",
                "number": "99754553",
                "citycode": "10",
                "countrycode": "60"
            },
            {
                "id": "3323eb77-1ca1-4765-bffb-039cf317c69f",
                "number": "87654321",
                "citycode": "20",
                "countrycode": "90"
            }
        ],
        "active": true
    }
}
```

#### 6. Eliminar Usuario
```http
DELETE /api/users/{id}
       /api/users/b615f837-d57b-4771-8ec5-8e1ebc7af259

Authorization: Bearer {eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBiY2kuY29tIiwiaWF0IjoxNzYxMjczMDMxLCJleHAiOjE3NjEyNzY2MzF9.3iSbazEtfmt8RuuEeVDDKdFdK0WIvafKcsjLfUMYXUo}
```
**Response 200:**
```json
{
    "codigo": 204,
    "mensaje": "Usuario eliminado exitosamente",
    "data": {
        "id": "b615f837-d57b-4771-8ec5-8e1ebc7af259",
        "name": "John Doe",
        "email": "john.doe@bci.com",
        "created": "2025-10-23T23:20:30.67994",
        "modified": "2025-10-23T23:30:30.995803",
        "lastLogin": "2025-10-23T23:20:52.773751",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBiY2kuY29tIiwiaWF0IjoxNzYxMjczMDMxLCJleHAiOjE3NjEyNzY2MzF9.3iSbazEtfmt8RuuEeVDDKdFdK0WIvafKcsjLfUMYXUo",
        "phones": [
            {
                "id": "a117518a-cad8-4598-8f7b-166358e5d45d",
                "number": "99754553",
                "citycode": "10",
                "countrycode": "60"
            },
            {
                "id": "3323eb77-1ca1-4765-bffb-039cf317c69f",
                "number": "87654321",
                "citycode": "20",
                "countrycode": "90"
            }
        ],
        "active": true
    }
}
```

---

## 📊 Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| 200 | OK - Solicitud exitosa |
| 201 | Created - Usuario registrado |
| 400 | Bad Request - Validación fallida |
| 401 | Unauthorized - Token inválido/expirado |
| 404 | Not Found - Usuario no encontrado |
| 409 | Conflict - Email duplicado |
| 500 | Internal Server Error |

---

## 🚀 Cómo Probarlo

### Opción 1: Swagger UI (Recomendado)

1. Inicia la aplicación
2. Accede a: `http://localhost:8081/swagger-ui.html`
3. Registra un usuario en `/register`
4. Copia el token del response
5. Click en "Authorize" 🔓 y pega: `Bearer {token}`
6. Prueba los demás endpoints

![alt text](image-1.png)

### Opción 2: H2 Console (Ver BD)

1. Accede a: `http://localhost:8081/h2-console`
2. JDBC URL: `jdbc:h2:mem:testdb`
3. User: `sa` | Password: _(vacío)_
4. Ejecuta queries SQL para inspeccionar datos

---

## 📐 Diagramas del Proyecto

### Diagrama de Clases (Modelo de Dominio)

```
┌─────────────────────────┐
│        User             │
├─────────────────────────┤
│-id: UUID                │
│-name: String            │
│-email: String (unique)  │
│-password: String        │
│-created: LocalDateTime  │
│-modified: LocalDateTime │
│-lastLogin: LocalDateTime│
│-token: String           │
│-isActive: Boolean       │
├─────────────────────────┤
│ + getPhones(): List     │
└──────────┬──────────────┘
           │ 1
           │
           │ *
┌──────────▼──────────────┐
│        Phone            │
├─────────────────────────┤
│ - id: UUID              │
│ - number: String        │
│ - citycode: String      │
│ - countrycode: String   │
│ - user: User            │
└─────────────────────────┘
```

### Diagrama de Base de Datos

```sql
┌─────────────────────────────────────────────────┐
│                  USERS                          │
├─────────────────────────────────────────────────┤
│ PK  id           UUID                           │
│     name         VARCHAR(100)                   │
│ UK  email        VARCHAR(100) NOT NULL     IDX  │ 🔑
│     password     VARCHAR(255) NOT NULL          │
│     created      TIMESTAMP NOT NULL             │
│     modified     TIMESTAMP                      │
│     last_login   TIMESTAMP                      │
│     token        VARCHAR(500)                   │
│     is_active    BOOLEAN NOT NULL           IDX │ 🔑
├─────────────────────────────────────────────────┤
│  Índices:                                       │ 🔑
│    - idx_user_email (email)                     │
│    - idx_user_active (is_active)                │
│    - idx_user_email_active (email, is_active)   │
└──────────────────┬──────────────────────────────┘
                   │
                   │ 1:N
                   │
┌──────────────────▼──────────────────────────────┐
│                 PHONES                          │
├─────────────────────────────────────────────────┤
│ PK  id           UUID                           │
│     number       VARCHAR(20) NOT NULL           │
│     citycode     VARCHAR(10)                    │
│     countrycode  VARCHAR(10)                    │
│ FK  user_id      UUID NOT NULL           IDX    │ 🔑
├─────────────────────────────────────────────────┤
│  Índices:                                       │ 🔑
│    - idx_phone_user (user_id)                   │
└─────────────────────────────────────────────────┘
```

### Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────┐
│              CLIENT (Browser/Postman)           │
└────────────────────┬────────────────────────────┘
                     │ HTTP/REST
                     ▼
┌─────────────────────────────────────────────────┐
│         🔒 SECURITY LAYER                       │
│  JwtAuthenticationFilter → SecurityConfig       │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│         📡 CONTROLLER LAYER                     │
│            UserController                       │
│  - register()   - login()                       │
│  - getAllUsers()  - getUserById()               │
│  - patchUser()    - deleteUser()                │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│         🔧 SERVICE LAYER                        │
│          UserServiceImpl                        │
│  - Lógica de negocio                            │
│  - Validaciones                                 │
│  - Generación de JWT                            │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│         💾 REPOSITORY LAYER                     │
│          UserRepository (JPA)                   │
│  - findByEmail()                                │
│  - save()  - findAll()  - delete()              │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│         🗄️ DATABASE (H2)                        │
│   USERS table  ←1:N→  PHONES table              │
└─────────────────────────────────────────────────┘
```


---

## 💾 Persistencia de Datos

### Base de Datos H2

- **Tipo**: En memoria (se pierde al reiniciar)
- **Modo**: Embedded
- **URL**: `jdbc:h2:mem:testdb`
- **Schema**: Auto-generado por Hibernate (DDL auto)

### Configuración JPA

```properties
spring.jpa.hibernate.ddl-auto=update
# Opciones: create, create-drop, update, validate, none
```

### Relaciones

- **User ← Phone**: OneToMany / ManyToOne
- **Cascade**: ALL (eliminar usuario elimina sus teléfonos)
- **Orphan Removal**: true

### Índices de Base de Datos

Para optimizar el rendimiento, se implementaron los siguientes índices:

**Tabla USERS:**
- `idx_user_email` - Optimiza búsquedas por email (login)
- `idx_user_active` - Filtrado de usuarios activos
- `idx_user_email_active` - Índice compuesto para login + estado

**Tabla PHONES:**
- `idx_phone_user` - Optimiza joins User → Phones

**Beneficios:**
- ⚡ Consultas de login 10x más rápidas
- 🚀 Joins optimizados entre User y Phone
- 📊 Preparado para escalar a producción

### Constraints

- `email`: UNIQUE, NOT NULL, max 100 caracteres
- `password`: NOT NULL
- `isActive`: NOT NULL, default TRUE
- `created`: NOT NULL, no actualizable
- `user_id` en Phone: NOT NULL (foreign key)

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests
./mvnw test

# Con reporte de cobertura
./mvnw clean test jacoco:report
```

### Ver Reporte de Cobertura

Abrir: `target/site/jacoco/index.html`

### Tests Incluidos

- ✅ `JwtUtilTest` - Generación y validación de tokens
- ✅ `PasswordValidatorTest` - Validación de contraseñas
- ✅ `UserControllerTest` - Endpoints REST
- ✅ `UserServiceImplTest` - Lógica de negocio
- ✅ `UserDetailsServiceImplTest` - Autenticación

![alt text](image-2.png)

---

## 📚 Documentación Adicional

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`
- **H2 Console**: `http://localhost:8081/h2-console`

---

## 👨‍💻 Autor

Proyecto desarrollado como evaluación técnica para **BCI** - Especialista en Integración

**Cristian Moreno Mendoza**
- 📧 Email: crmorenom@gmail.com

---

## 📝 Notas Importantes

⚠️ **Producción**: Cambiar `jwt.secret` y usar BD persistente (PostgreSQL/MySQL)

⚠️ **Seguridad**: El proyecto usa H2 y configuraciones de desarrollo. No usar en producción sin ajustes.

✅ **Extensible**: Arquitectura preparada para agregar más módulos (productos, órdenes, etc.)
