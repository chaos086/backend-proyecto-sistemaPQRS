# Sistema de PQRS - Universidad del Quindío

## Integrantes

- **María José Vásquez Betancur**
- **Diego Hernán Giraldo Cifuentes**
- **Alejandro Marín Hernández**

*Estudiantes de Ingeniería de Sistemas - Universidad del Quindío*

---

## Descripción del Proyecto

Este proyecto corresponde al desarrollo de un sistema de **Peticiones, Quejas, Reclamos y Sugerencias (PQRS)** para la Universidad del Quindío, implementado como parte del curso de **Programación Avanzada**.

El sistema permite a los estudiantes, docentes y demás usuarios de la institución registrar sus solicitudes, las cuales son gestionadas por coordinadores y docentes responsables hasta su resolución. El proyecto sigue los principios de **Domain-Driven Design (DDD)** para garantizar una arquitectura robusta y mantenible.

### Funcionalidades Principales

- **Registro de Solicitudes**: Los usuarios pueden crear solicitudes (peticiones, quejas, reclamos o sugerencias) a través de diferentes canales.
- **Clasificación y Priorización**: Los coordinadores pueden clasificar el tipo de solicitud y asignar una prioridad.
- **Asignación de Responsable**: Un docente es asignado para atender la solicitud.
- **Seguimiento**: Se mantiene un historial completo de cada solicitud para trazabilidad.
- **Gestión de Estados**: Control del ciclo de vida de cada solicitud (Registrada → Clasificada → En Atención → Atendida → Cerrada).

### Reglas de Negocio

- Un solicitante no puede tener más de **5 solicitudes pendientes** simultáneamente.
- Un profesor no puede tener más de **10 solicitudes en atención** simultáneamente.
- Solo usuarios activos pueden crear o atender solicitudes.
- Solo los docentes pueden ser asignados como responsables.

---

## Tecnologías Utilizadas

- **Lenguaje**: Java 25
- **Framework**: Spring Boot 4.0.2
- **Arquitectura**: Domain-Driven Design (DDD)
- **Base de Datos**: H2 (en memoria)
- **Pruebas**: JUnit 5

---

## Estructura del Proyecto

```
proyecto/
├── src main/
│  /
│   ├── │   └── java/co/edu/uniquindio/proyecto/
│   │       ├── domain/
│   │       │   ├── entity/          # Entidades del dominio
│   │       │   ├── valueobject/     # Value Objects y Enums
│   │       │   ├── exception/       # Excepciones del dominio
│   │       │   └── service/          # Domain Services
│   │       ├── application/          # Application Services
│   │       ├── infrastructure/      # Controllers y Repositorios
│   │       └── config/              # Configuración de Spring
│   └── test/
│       └── java/.../domain/         # Pruebas unitarias del dominio
├── build.gradle
├── gradlew
├── gradlew.bat
└── README.md
```
---

## Compilación y Ejecución

### Ejecución con UN SOLO CLIC (Botón)

#### Windows 11 (Doble clic):
simplemente haz **doble clic** en el archivo:

```
compilar_y_probar.bat
```

ubicado en la raíz del proyecto. ¡Listo! ✅

#### Linux/Mac (Doble clic o terminal):
Ejecuta el script desde la terminal:

```bash
# Dar permisos (solo una vez)
chmod +x compilar_y_probar.sh

# Ejecutar
./compilar_y_probar.sh
```

O simplemente haz doble clic en el archivo si tu entorno lo permite.

---

### 📋 Manual (Opción por comandos)

### Prerrequisitos

- JDK 25 instalado en el sistema
- Gradle instalado (opcional, se incluye wrapper)

### Compilar el Proyecto

**Windows (PowerShell):**
```powershell
.\gradlew compileJava
```

**Windows (CMD):**
```cmd
gradlew compileJava
```

**Linux/Mac:**
```bash
./gradlew compileJava
```

### Ejecutar las Pruebas

**Windows (PowerShell):**
```powershell
.\gradlew test
```

**Windows (CMD):**
```cmd
gradlew test
```

**Linux/Mac:**
```bash
./gradlew test
```

### Ver Reporte de Pruebas

Después de ejecutar las pruebas, el reporte se encuentra en:

```
build/reports/tests/test/index.html
```

### Ejecutar la Aplicación

**Windows (PowerShell):**
```powershell
.\gradlew bootRun
```

**Windows (CMD):**
```cmd
gradlew bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8080`

---

## Endpoints de la API

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión y obtener token JWT |

### Usuarios

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/usuarios` | Crear usuario |
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID |
| PUT | `/api/usuarios/{id}/activar` | Activar usuario |
| PUT | `/api/usuarios/{id}/desactivar` | Desactivar usuario |

### Solicitudes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/solicitudes` | Crear solicitud |
| GET | `/api/solicitudes` | Listar solicitudes |
| GET | `/api/solicitudes/{id}` | Obtener solicitud por ID |
| GET | `/api/solicitudes/solicitante/{id}` | Listar solicitudes de un solicitante |
| PUT | `/api/solicitudes/{id}/clasificar` | Clasificar solicitud |
| PUT | `/api/solicitudes/{id}/priorizar` | Priorizar solicitud |
| PUT | `/api/solicitudes/{id}/asignar-responsable` | Asignar responsable |
| PUT | `/api/solicitudes/{id}/atender` | Marcar como atendida |
| PUT | `/api/solicitudes/{id}/cerrar` | Cerrar solicitud |

---

## Estado de Entregas

| Entrega | Estado | Porcentaje |
|---------|--------|-------------|
| Entrega #1 (Modelado + Comportamiento + Tests) | ✅ Completo | 100% |
| Entrega #2 (Arquitectura + Persistencia + API REST + Seguridad) | ✅ Completo | 100% |

---

## Entrega #2: Arquitectura, Persistencia y Seguridad

### Componentes Implementados

#### Arquitectura DDD con Capas
- **Domain Layer**: Entidades, Value Objects, Servicios de Dominio, Excepciones
- **Application Layer**: UseCases (CASO DE USO), Application Services
- **Infrastructure Layer**: Controllers REST, Repositorios, Mappers, Seguridad

#### UseCases Implementados (15 total)
| # | UseCase | Descripción |
|---|--------|-------------|
| 1 | CrearUsuarioUseCase | Crea un nuevo usuario en el sistema |
| 2 | ObtenerUsuarioUseCase | Busca usuario por ID |
| 3 | ListarUsuariosUseCase | Lista todos los usuarios |
| 4 | ActivarUsuarioUseCase | Activa un usuario inactivo |
| 5 | DesactivarUsuarioUseCase | Desactiva un usuario activo |
| 6 | CrearSolicitudUseCase | Crea una nueva solicitud PQRS |
| 7 | ObtenerSolicitudUseCase | Busca solicitud por ID |
| 8 | ListarSolicitudesUseCase | Lista todas las solicitudes |
| 9 | ListarSolicitudesPorSolicitanteUseCase | Lista solicitudes de un solicitante |
| 10 | ConsultarSolicitudesPorEstadoUseCase | Filtra solicitudes por estado |
| 11 | ClasificarSolicitudUseCase | Clasifica tipo de solicitud (coordinador) |
| 12 | PriorizarSolicitudUseCase | Asigna prioridad (coordinador) |
| 13 | AsignarResponsableUseCase | Asigna docente responsable |
| 14 | CambiarEstadoUseCase | Cambia estado de la solicitud |
| 15 | CerrarSolicitudUseCase | Cierra una solicitud |

#### Persistencia H2
- **Base de datos en memoria**: H2 configurada
- **Repositorios**: Implementación dual con Switch de Perfiles
  - `@Profile("memory")`: Repositorios InMemory (para testing)
  - `@Profile("jpa")`: Repositorios JPA (para producción)
- **Entidades JPA**: Mapeo completo con anotaciones Jakarta Persistence
- **H2 Console**: Disponible en `/h2-console`

#### Acceso a Consola H2
La consola H2 está habilitada para consultar la base de datos durante desarrollo:

| Configuración | Valor |
|---------------|------|
| URL Consola | `http://localhost:8080/h2-console` |
| JDBC URL | `jdbc:h2:mem:proyectodb` |
| Username | `sa` |
| Password | *(vacío)* |

#### Credenciales Seed (Datos de Prueba Predefinidos)
El sistema se inicia con usuarios de prueba para desarrollo:

| Email | Password | Rol |
|------|----------|-----|
| `admin@uniquindio.edu.co` | `Admin#12345` | ADMIN |
| `coordinador@uniquindio.edu.co` | `Coord#12345` | COORDINADOR |
| `docente@uniquindio.edu.co` | `Docen#12345` | DOCENTE |
| `estudiante@uniquindio.edu.co` | `Estu#12345` | ESTUDIANTE |

#### Login JWT
Para autenticarse en la API:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@uniquindio.edu.co","password":"Admin#12345"}'
```

Respuesta:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresInSeconds": 14400
}
```

Para usar los endpoints protegidos, incluir el token en el header:
```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer <accessToken>"
```

#### API REST con Swagger/OpenAPI
- **Documentación**: SpringDoc OpenAPI 3.0.3
- **Swagger UI**: Disponible en `/swagger-ui.html`
- **OpenAPI JSON**: Disponible en `/v3/api-docs`
- **Validación**: Bean Validation con mensajes personalizados

#### Seguridad JWT
- **Autenticación**: Token JWT (Bearer)
- **Filtro**: JwtAuthenticationFilter
- **Servicio**: JwtService para generación y validación
- **Configuración**: SecurityConfiguration con rutas públicas configuradas

#### Controllers REST
| Controller | Endpoints |
|------------|-----------|
| UsuarioController | CRUD de usuarios |
| SolicitudController | Gestión completa de solicitudes PQRS |
| AuthController | Login y generación de tokens JWT |

#### DTOs y Mapeo
- **DTOs**: 14 objetos para request/response
- **Mappers**: MapStruct para transformación entidad ↔ DTO

### Tecnologías Segunda Entrega
- **Java**: 25
- **Spring Boot**: 4.0.2
- **Spring Security**: 6.x
- **SpringDoc OpenAPI**: 3.0.3
- **MapStruct**: 1.6.3
- **H2 Database**: En memoria
- **jjwt**: 0.12.5

---

## Licencia

Este proyecto es desarrollado con fines académicos para la Universidad del Quindío.
