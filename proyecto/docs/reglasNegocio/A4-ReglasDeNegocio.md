# Reglas de Negocio.

## Sistema de PQRS - Universidad del Quindío

**Versión:** 1.0  
**Fecha:** Marzo 2026  
**Autores:** 
- María José Vásquez Betancur, 
- Diego Hernán Giraldo Cifuentes, 
- Alejandro Marín Hernández  

**Curso:**
- Programación Avanzada - Universidad del Quindío


---

## 1. Introducción

Acá describimos las reglas de negocio implementadas en el sistema de PQRS (Peticiones, Quejas, Reclamos y Sugerencias) de la Universidad del Quindío. Las reglas están encapsuladas en el dominio de la aplicación, siguiendo los principios de Domain-Driven Design (DDD).

### 1.1 Propósito

Este documento es detallar las reglas de negocio que gobiernan el comportamiento del sistema, implementación en código, y su relación con las entidades, value objects y servicios del dominio.

### 1.2 Alcance

Este documento cubre:
- Las 7 reglas de negocio principales
- Su implementación en el código del dominio
- Las clases y métodos involucrados
- Las transiciones de estado del sistema

---

## 2. Reglas de Negocio

### Regla 1: Límite de Solicitudes Pendientes por Solicitante

| Aspecto | Detalle |
|---------|----------|
| **Acción que regula** | Crear nueva solicitud |
| **Condición** | El solicitante debe estar activo y no puede tener más de 5 solicitudes en estado REGISTRADA, CLASIFICADA o EN_ATENCION |
| **Comportamiento** | Si el solicitante supera el límite, se lanza `BusinessRuleViolation` |
| **Clase responsable** | `SolicitudDomainService` |
| **Método** | `validarCrearSolicitud(Usuario solicitante, List<Solicitud> solicitudesExistentes)` |

**Código relevante:**
```java
// En SolicitudDomainService.java
private static final int MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE = 5;

public void validarCrearSolicitud(Usuario solicitante, List<Solicitud> solicitudesExistentes) {
    if (!solicitante.activo()) {
        throw new BusinessRuleViolation("El solicitante debe estar activo");
    }
    
    long solicitudesPendientes = solicitudesExistentes.stream()
        .filter(s -> s.solicitante().value().equals(solicitante.id().value()))
        .filter(s -> s.estado() == EstadoSolicitud.REGISTRADA || 
                     s.estado() == EstadoSolicitud.CLASIFICADA ||
                     s.estado() == EstadoSolicitud.EN_ATENCION)
        .count();

    if (solicitudesPendientes >= MAX_SOLICITUDES_PENDIENTES_POR_SOLICITANTE) {
        throw new BusinessRuleViolation("Un solicitante no puede tener más de 5 solicitudes pendientes");
    }
}
```

---

### Regla 2: Límite de Solicitudes en Atención por Profesor

| Aspecto | Detalle |
|---------|---------|
| **Acción que regula** | Asignar responsable (profesor) a una solicitud |
| **Condición** | El profesor debe estar activo, tener rol PROFESOR, y no puede tener más de 10 solicitudes en estado EN_ATENCION |
| **Comportamiento** | Si el profesor supera el límite, se lanza `DomainException` |
| **Clase responsable** | `SolicitudDomainService` |
| **Método** | `validarAsignarResponsable(Usuario responsable, List<Solicitud> solicitudesExistentes)` |

**Código relevante:**
```java
// En SolicitudDomainService.java
private static final int MAX_SOLICITUDES_EN_ATENCION_POR_PROFESOR = 10;

public void validarAsignarResponsable(Usuario responsable, List<Solicitud> solicitudesExistentes) {
    if (!responsable.activo()) {
        throw new DomainException("El responsable debe estar activo");
    }
    if (responsable.rol() != Rol.PROFESOR) {
        throw new DomainException("Solo un profesor puede ser asignado como responsable");
    }

    long solicitudesEnAtencion = solicitudesExistentes.stream()
        .filter(s -> s.responsableId() != null)
        .filter(s -> s.responsableId().toString().equals(responsable.id().valor()))
        .filter(s -> s.estado() == EstadoSolicitud.EN_ATENCION)
        .count();

    if (solicitudesEnAtencion >= MAX_SOLICITUDES_EN_ATENCION_POR_PROFESOR) {
        throw new DomainException("Un profesor no puede tener más de 10 solicitudes en atención");
    }
}
```

---

### Regla 3: Solo Usuarios Activos Pueden Crear o Atender

| Aspecto | Detalle |
|---------|---------|
| **Acción que regula** | Crear solicitud, Atender solicitud |
| **Condición** | El usuario (solicitante o responsable) debe tener estado `ACTIVO` (enum `EstadoUsuario`) |
| **Comportamiento** | Si el usuario está inactivo, se lanza `DomainException` |
| **Clases responsables** | `SolicitudDomainService`, `Solicitud` |
| **Métodos** | `validarCrearSolicitud`, `asignarResponsable` |
| **Enum** | `EstadoUsuario` (ACTIVO, INACTIVO) |

**Código relevante:**
```java
// Enum EstadoUsuario.java
public enum EstadoUsuario {
    ACTIVO,
    INACTIVO
}

// En Solicitud.java - asignarResponsable()
public void asignarResponsable(UUID responsableId, String nombreResponsable, 
                              UUID coordinadorId, String nombreCoordinador) {
    asegurarNoCerrada();
    if (responsableId == null) throw new DomainException("Responsable es obligatorio");

    if (estado != EstadoSolicitud.CLASIFICADA)
        throw new DomainException("Solo se puede asignar responsable en estado CLASIFICADA");

    this.responsableId = responsableId;
    this.nombreResponsable = nombreResponsable;
    this.estado = EstadoSolicitud.EN_ATENCION;
    historialService.registrarHistorial(this.id, AccionHistorial.ASIGNAR_RESPONSABLE, 
        coordinadorId, nombreCoordinador, "Responsable: " + nombreResponsable);
}
```

---

### Regla 4: Solo Profesores Pueden Ser Responsables

| Aspecto | Detalle |
|---------|---------|
| **Acción que regula** | Asignar responsable |
| **Condición** | El usuario asignado debe tener rol PROFESOR |
| **Comportamiento** | Si el usuario no es profesor, se lanza `DomainException` |
| **Clase responsable** | `SolicitudDomainService` |
| **Método** | `validarAsignarResponsable` |

**Código relevante:**
```java
// En SolicitudDomainService.java
if (responsable.rol() != Rol.PROFESOR) {
    throw new DomainException("Solo un profesor puede ser asignado como responsable");
}
```

---

### Regla 5: Transiciones de Estado Válidas

| Aspecto | Detalle |
|---------|---------|
| **Acción que regula** | Clasificar, Priorizar, Asignar Responsable, Atender, Cerrar |
| **Condición** | Solo se puede ejecutar la acción si la solicitud está en el estado correcto |
| **Comportamiento** | Si el estado no es el correcto, se lanza `DomainException` |
| **Clase responsable** | `Solicitud` |
| **Métodos** | `clasificar()`, `priorizar()`, `asignarResponsable()`, `marcarAtendida()`, `cerrar()` |

**Código relevante:**
```java
// En Solicitud.java - clasificar()
public void clasificar(TipoSolicitud tipo, UUID coordinadorId, String nombreCoordinador) {
    asegurarNoCerrada();
    if (estado != EstadoSolicitud.REGISTRADA)
        throw new DomainException("Solo se puede clasificar una solicitud en estado REGISTRADA");
    // ...
}

// Transiciones válidas:
// REGISTRADA -> CLASIFICADA (clasificar)
// CLASIFICADA -> EN_ATENCION (asignarResponsable)
// EN_ATENCION -> ATENDIDA (marcarAtendida)
// ATENDIDA -> CERRADA (cerrar)
```

---

### Regla 6: No Modificar una Solicitud Cerrada

| Aspecto | Detalle |
|---------|---------|
| **Acción que regulate** | Cualquier modificación sobre una solicitud |
| **Condición** | La solicitud no debe estar en estado CERRADA |
| **Comportamiento** | Si está cerrada, se lanza `DomainException` |
| **Clase responsable** | `Solicitud` |
| **Método** | `asegurarNoCerrada()` |

**Código relevante:**
```java
// En Solicitud.java
private void asegurarNoCerrada() {
    if (estado == EstadoSolicitud.CERRADA)
        throw new DomainException("Una solicitud CERRADA no puede modificarse");
}
```

---

### Regla 7: Registro de Historial (Auditoría)

| Aspecto | Detalle |
|---------|---------|
| **Acción que regula** | Todas las acciones relevantes |
| **Condición** | Cada acción debe generar una entrada en el historial |
| **Comportamiento** | Se crea un `EntradaHistorial` (record) con ID, fecha, acción, usuario y observación |
| **Clases responsables** | `HistorialService`, `EntradaHistorial` |
| **Enum** | `AccionHistorial` |

**Código relevante:**
```java
// Enum AccionHistorial.java
public enum AccionHistorial {
    REGISTRAR_SOLICITUD,
    CLASIFICAR_SOLICITUD,
    PRIORIZAR_SOLICITUD,
    ASIGNAR_RESPONSABLE,
    MARCAR_ATENDIDA,
    CERRAR_SOLICITUD
}

// EntradaHistorial es un record con ID
public record EntradaHistorial(
    String id,
    Instant fecha,
    AccionHistorial accion,
    String usuarioId,
    String nombreUsuario,
    String observacion
) {}

// HistorialService.java - Servicio del dominio
public class HistorialService {
    public void registrarHistorial(SolicitudId solicitudId, AccionHistorial accion,
                                   UUID usuarioId, String nombreUsuario, String observacion) {
        EntradaHistorial entrada = new EntradaHistorial(
            UUID.randomUUID().toString(),
            Instant.now(),
            accion,
            usuarioId.toString(),
            nombreUsuario,
            observacion
        );
        // Registra en lista de entradas del historial
    }
}
```

---

## 3. Máquina de Estados

```
┌─────────────┐     clasificar()     ┌─────────────┐
│  REGISTRADA │ ──────────────────►  │ CLASIFICADA │
└─────────────┘                      └──────┬──────┘
                                            │
                                        priorizar()
                                            │
                                            ▼
                            ┌────────────────────────────────┐
                            │    (con prioridad asignada)    │
                            └────────────────────────────────┘
                                            │
                                  asignarResponsable()
                                            ▼
┌─────────────┐     marcarAtendida()  ┌───────────┐     cerrar()   ┌─────────┐
│ EN_ATENCION │ ──────────────────►   │  ATENDIDA │ ────────────►  │ CERRADA │
└─────────────┘                       └───────────┘                └─────────┘
```

---

## 4. Arquitectura y Diseño

### 4.1 Principio de Dominio Puro

El dominio NO depende de ninguna infraestructura (Spring, bases de datos, etc.):

- **Entidades:** `Usuario`, `Solicitud`, `EntradaHistorial` (record)
- **Value Objects:** `Email`, `IdentificacionUsuario`, `DescripcionSolicitud`, `JustificacionPrioridad`, `SolicitudId`, `IdentificacionSolicitante`
- **Domain Services:** `SolicitudDomainService`, `HistorialService` (Sin anotaciones Spring)
- **Excepciones:** `DomainException`
- **Config:** `DomainServiceConfig`

### 4.2 Inyección de Dependencias

Los Domain Services se inyectan desde la capa de aplicación mediante configuración:

```java
// DomainServiceConfig.java
@Configuration
public class DomainServiceConfig {
    @Bean
    public SolicitudDomainService solicitudDomainService() {
        return new SolicitudDomainService();
    }
    
    @Bean
    public HistorialService historialService() {
        return new HistorialService();
    }
}
```

### 4.3 Value Objects Inmutables

Todos los VO son `record` con validación en el constructor:

```java
// Ejemplo: Email.java
public record Email(String valor) {
    public Email {
        if (valor == null || valor.isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (!valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new DomainException("El formato del email es inválido");
        }
    }
}
```

---

## 5. Clases y Métodos del Dominio

### 5.1 Entidades

| Entidad | Responsabilidad |
|---------|-----------------|
| `Usuario` | Representa un usuario del sistema con rol (ESTUDIANTE, PROFESOR, ADMINISTRATIVO, COORDINADOR) y estado (ACTIVO, INACTIVO) |
| `Solicitud` | Raíz del agregado, gestiona el ciclo de vida de una solicitud |
| `EntradaHistorial` | Record que representa una entrada de auditoría en el historial |

### 5.2 Value Objects

| VO | Descripción |
|----|-------------|
| `Email` | Correo electrónico validado |
| `IdentificacionUsuario` | UUID tipado para identificar usuarios |
| `DescripcionSolicitud` | Descripción de la solicitud (10-500 caracteres) |
| `JustificacionPrioridad` | Justificación de prioridad (5-300 caracteres) |
| `SolicitudId` | UUID tipado para identificar solicitudes |
| `IdentificacionSolicitante` | Identificación del solicitante |

### 5.3 Excepciones del Dominio

| Excepción | Uso |
|-----------|-----|
| `DomainException` | Excepción base para errores de dominio y violaciones de reglas de negocio |

### 5.4 Enums

| Enum | Contenido |
|------|-----------|
| `AccionHistorial` | REGISTRAR_SOLICITUD, CLASIFICAR_SOLICITUD, PRIORIZAR_SOLICITUD, ASIGNAR_RESPONSABLE, MARCAR_ATENDIDA, CERRAR_SOLICITUD |
| `CanalOrigen` | PRESENCIAL, TELEFONICO, CORREO_ELECTRONICO, APLICACION_WEB, APLICACION_MOVIL |
| `EstadoSolicitud` | REGISTRADA, CLASIFICADA, EN_ATENCION, ATENDIDA, CERRADA |
| `EstadoUsuario` | ACTIVO, INACTIVO |
| `Prioridad` | BAJA, MEDIA, ALTA |
| `Rol` | ESTUDIANTE, PROFESOR, ADMINISTRATIVO, COORDINADOR |
| `TipoSolicitud` | QUEJA, RECLAMO, SUGERENCIA, PETICION, FELICITACION |

---

## 6. Domain Services

### 6.1 SolicitudDomainService

Gestiona las validaciones de negocio relacionadas con las solicitudes:

- `validarCrearSolicitud()` - Valida que el solicitante pueda crear una nueva solicitud
- `validarClasificar()` - Valida que la solicitud pueda ser clasificada
- `validarPriorizar()` - Valida que la solicitud pueda ser priorizada
- `validarAsignarResponsable()` - Valida que se pueda asignar un responsable
- `validarMarcarAtendida()` - Valida que la solicitud pueda ser marcada como atendida
- `validarCerrar()` - Valida que la solicitud pueda ser cerrada
- `obtenerSolicitudesPorSolicitante()` - Obtiene solicitudes de un solicitante
- `obtenerSolicitudesPorResponsable()` - Obtiene solicitudes asignadas a un responsable

### 6.2 HistorialService

Gestiona el registro de entradas en el historial de auditoría:

- `registrarHistorial()` - Registra una nueva entrada en el historial de una solicitud

---

## 7. Pruebas Unitarias del Dominio

El dominio cuenta con **70 pruebas unitarias** que validan las reglas de negocio, value objects, enums y excepciones:

### 7.1 Tests de Entidades

#### UsuarioTest (8 pruebas)
- ✅ crearUsuario_valido
- ✅ crearUsuario_nombreNulo
- ✅ crearUsuario_nombreBlanco
- ✅ crearUsuario_rolNulo
- ✅ crearUsuario_inactivo
- ✅ desactivarUsuario
- ✅ activarUsuario
- ✅ agregarSolicitudRegistradaYVerificar

#### SolicitudTest (9 pruebas)
- ✅ crearSolicitud_valida
- ✅ crearSolicitud_solicitanteNulo
- ✅ crearSolicitud_canalNulo
- ✅ estadoInicial_registrada
- ✅ clasificar_enEstadoRegistrada
- ✅ priorizar_enEstadoClasificada
- ✅ asignarResponsable_funciona
- ✅ marcarAtendida_funciona
- ✅ cerrar_funciona

#### EntradaHistorialTest (1 prueba)
- ✅ crearEntradaHistorial_valida

### 7.2 Tests de Value Objects

#### EmailTest (6 pruebas)
- ✅ crearEmailValido
- ✅ crearEmailNuloDebeLanzarExcepcion
- ✅ crearEmailVacioDebeLanzarExcepcion
- ✅ crearEmailSinArrobaDebeLanzarExcepcion
- ✅ crearEmailSinDominioDebeLanzarExcepcion
- ✅ crearEmailConEspaciosDebeLanzarExcepcion

#### IdentificacionUsuarioTest (6 pruebas)
- ✅ crearIdentificacionValida
- ✅ crearIdentificacionNulaDebeLanzarExcepcion
- ✅ crearIdentificacionVaciaDebeLanzarExcepcion
- ✅ crearIdentificacionEnBlancoDebeLanzarExcepcion
- ✅ crearIdentificacionNewId
- ✅ crearIdentificacionFromUUID

#### DescripcionSolicitudTest (7 pruebas)
- ✅ crearDescripcionValida
- ✅ crearDescripcionNulaDebeLanzarExcepcion
- ✅ crearDescripcionVaciaDebeLanzarExcepcion
- ✅ crearDescripcionMuyCortaDebeLanzarExcepcion
- ✅ crearDescripcionMuyLargaDebeLanzarExcepcion
- ✅ crearDescripcionExactaMinima
- ✅ crearDescripcionExactaMaxima

#### JustificacionPrioridadTest (7 pruebas)
- ✅ crearJustificacionValida
- ✅ crearJustificacionNulaDebeLanzarExcepcion
- ✅ crearJustificacionVaciaDebeLanzarExcepcion
- ✅ crearJustificacionMuyCortaDebeLanzarExcepcion
- ✅ crearJustificacionMuyLargaDebeLanzarExcepcion
- ✅ crearJustificacionExactaMinima
- ✅ crearJustificacionExactaMaxima

#### SolicitudIdTest (5 pruebas)
- ✅ crearSolicitudIdValido
- ✅ crearSolicitudIdNuloDebeLanzarExcepcion
- ✅ crearSolicitudIdVacioDebeLanzarExcepcion
- ✅ crearSolicitudIdNewId
- ✅ crearSolicitudIdFromUUID

#### IdentificacionSolicitanteTest (4 pruebas)
- ✅ crearIdentificacionValida
- ✅ crearIdentificacionNulaDebeLanzarExcepcion
- ✅ crearIdentificacionVaciaDebeLanzarExcepcion
- ✅ crearIdentificacionEnBlancoDebeLanzarExcepcion

### 7.3 Tests de Enums

#### EstadoUsuarioTest (4 pruebas)
- ✅ valoresDebenExistir
- ✅ activoEsActivo
- ✅ inactivoEsInactivo
- ✅ cantidadDeValores

#### AccionHistorialTest (2 pruebas)
- ✅ valoresDebenExistir
- ✅ cantidadDeValores

#### CanalOrigenTest (2 pruebas)
- ✅ valoresDebenExistir
- ✅ cantidadDeValores

#### EstadoSolicitudTest (2 pruebas)
- ✅ valoresDebenExistir
- ✅ cantidadDeValores

#### PrioridadTest (2 pruebas)
- ✅ valoresDebenExistir
- ✅ cantidadDeValores

#### RolTest (2 pruebas)
- ✅ valoresDebenExistir
- ✅ cantidadDeValores

#### TipoSolicitudTest (2 pruebas)
- ✅ valoresDebenExistir
- ✅ cantidadDeValores

**Total: 70 pruebas unitarias**

---

## 8. Notas de Implementación

### 8.1 Decisiones de Diseño

1. **Dominio Puro:** El dominio no tiene dependencias de frameworks. Esto facilita las pruebas unitarias y la portabilidad.

2. **Value Objects como Records:** Usamos `record` de Java para VOs inmutables con validación automática.

3. **Enum para Acciones de Historial:** En lugar de strings, usamos un enum tipado para las acciones del historial.

4. **Domain Services como POJOs:** Los servicios de dominio no tienen anotaciones de Spring, se inyectan desde la configuración.

5. **EntradaHistorial como Record:** El historial de auditoría usa un `record` con ID único para trazabilidad.

6. **Enum EstadoUsuario:** Se usa un enum para el estado del usuario en lugar de un `boolean` para mejor legibilidad y extensibilidad.

7. **HistorialService Separado:** El servicio de historial se separó de la entidad `Solicitud` para seguir el principio de responsabilidad única.

### 8.2 Cambios de la Revisión del Profesor

| Corrección | Solución Implementada |
|------------|---------------------|
| EntradaHistorial | Convertido a `record` con ID (UUID, usuarioId, nombreUsuario) |
| Paquetes `valueObject` | Renombrado a `valueobject` (minúsculas) |
| UsuarioReferencia | Eliminado completamente |
| boolean activo | Creado enum `EstadoUsuario` (ACTIVO, INACTIVO) |
| Lista solicitudesRegistradas | Eliminada de Usuario |
| registrarHistorial() | Creado `HistorialService` en `domain.service` |
| Instant.now() en historial | Movido al constructor privado de EntradaHistorial |
| Validar estados en clasificar() | Agregada validación de estado REGISTRADA |

---

## 9. Referencias al Código

| Regla | Clase | Método |
|-------|-------|--------|
| Regla 1 | `SolicitudDomainService` | `validarCrearSolicitud` |
| Regla 2 | `SolicitudDomainService` | `validarAsignarResponsable` |
| Regla 3 | `SolicitudDomainService`, `Solicitud` | `validarCrearSolicitud`, `asignarResponsable` |
| Regla 4 | `SolicitudDomainService` | `validarAsignarResponsable` |
| Regla 5 | `Solicitud` | `clasificar`, `priorizar`, `asignarResponsable`, `marcarAtendida`, `cerrar` |
| Regla 6 | `Solicitud` | `asegurarNoCerrada` |
| Regla 7 | `HistorialService`, `EntradaHistorial` | `registrarHistorial`, constructor de `EntradaHistorial` |

---

## 10. Historial de Cambios

| Fecha | Versión | Descripción |
|-------|---------|-------------|
| Marzo 2026 | 1.0 | Versión inicial del documento |
| Marzo 2026 | 1.1 | Actualización: Agregado BAJA a Prioridad. Actualizado conteo de tests unitarios (25+). Documentados todos los tests de entidades, value objects y excepciones. |

---

*Documento generado para la entrega #1 del proyecto de Programación Avanzada - Universidad del Quindío*
