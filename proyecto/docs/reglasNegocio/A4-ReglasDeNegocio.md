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

### Regla 2: Límite de Solicitudes en Atención por Docente

| Aspecto | Detalle |
|---------|----------|
| **Acción que regula** | Asignar responsable (docente) a una solicitud |
| **Condición** | El docente debe estar activo, tener rol DOCENTE, y no puede tener más de 10 solicitudes en estado EN_ATENCION |
| **Comportamiento** | Si el docente supera el límite, se lanza `BusinessRuleViolation` |
| **Clase responsable** | `SolicitudDomainService` |
| **Método** | `validarAsignarResponsable(Usuario responsable, List<Solicitud> solicitudesExistentes)` |

**Código relevante:**
```java
// En SolicitudDomainService.java
private static final int MAX_SOLICITUDES_EN_ATENCION_POR_DOCENTE = 10;

public void validarAsignarResponsable(Usuario responsable, List<Solicitud> solicitudesExistentes) {
    if (!responsable.activo()) {
        throw new BusinessRuleViolation("El responsable debe estar activo");
    }
    if (responsable.rol() != Rol.DOCENTE) {
        throw new BusinessRuleViolation("Solo un docente puede ser asignado como responsable");
    }

    long solicitudesEnAtencion = solicitudesExistentes.stream()
        .filter(s -> s.responsable() != null)
        .filter(s -> s.responsable().value().equals(responsable.id().value()))
        .filter(s -> s.estado() == EstadoSolicitud.EN_ATENCION)
        .count();

    if (solicitudesEnAtencion >= MAX_SOLICITUDES_EN_ATENCION_POR_DOCENTE) {
        throw new BusinessRuleViolation("Un docente no puede tener más de 10 solicitudes en atención");
    }
}
```

---

### Regla 3: Solo Usuarios Activos Pueden Crear o Atender

| Aspecto | Detalle |
|---------|----------|
| **Acción que regula** | Crear solicitud, Atender solicitud |
| **Condición** | El usuario (solicitante o responsable) debe tener el flag `activo = true` |
| **Comportamiento** | Si el usuario está inactivo, se lanza `BusinessRuleViolation` |
| **Clases responsables** | `SolicitudDomainService`, `Solicitud` |
| **Métodos** | `validarCrearSolicitud`, `asignarResponsable` |

**Código relevante:**
```java
// En Solicitud.java - asignarResponsable()
public void asignarResponsable(Usuario responsable, UsuarioReferencia coordinador) {
    asegurarNoCerrada();
    if (responsable == null) throw new DomainException("Responsable es obligatorio");
    if (!responsable.activo())
        throw new BusinessRuleViolation("No se puede asignar un responsable inactivo");

    if (estado != EstadoSolicitud.CLASIFICADA)
        throw new BusinessRuleViolation("Solo se puede asignar responsable en estado CLASIFICADA");

    this.responsable = new UsuarioReferencia(responsable.id().value(), responsable.nombre());
    this.estado = EstadoSolicitud.EN_ATENCION;
    registrarHistorial(AccionHistorial.ASIGNAR_RESPONSABLE, coordinador, "Responsable: " + responsable.nombre());
}
```

---

### Regla 4: Solo Docentes Pueden Ser Responsables

| Aspecto | Detalle |
|---------|----------|
| **Acción que regula** | Asignar responsable |
| **Condición** | El usuario asignado debe tener rol DOCENTE |
| **Comportamiento** | Si el usuario no es docente, se lanza `BusinessRuleViolation` |
| **Clase responsable** | `SolicitudDomainService` |
| **Método** | `validarAsignarResponsable` |

**Código relevante:**
```java
// En SolicitudDomainService.java
if (responsable.rol() != Rol.DOCENTE) {
    throw new BusinessRuleViolation("Solo un docente puede ser asignado como responsable");
}
```

---

### Regla 5: Transiciones de Estado Válidas

| Aspecto | Detalle |
|---------|----------|
| **Acción que regula** | Clasificar, Priorizar, Asignar Responsable, Atender, Cerrar |
| **Condición** | Solo se puede ejecutar la acción si la solicitud está en el estado correcto |
| **Comportamiento** | Si el estado no es el correcto, se lanza `BusinessRuleViolation` |
| **Clase responsable** | `Solicitud` |
| **Métodos** | `clasificar()`, `priorizar()`, `asignarResponsable()`, `marcarAtendida()`, `cerrar()` |

**Código relevante:**
```java
// En Solicitud.java - clasificar()
public void clasificar(TipoSolicitud tipo, UsuarioReferencia coordinador) {
    asegurarNoCerrada();
    if (estado != EstadoSolicitud.REGISTRADA)
        throw new BusinessRuleViolation("Solo se puede clasificar una solicitud en estado REGISTRADA");
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
|---------|----------|
| **Acción que regulate** | Cualquier modificación sobre una solicitud |
| **Condición** | La solicitud no debe estar en estado CERRADA |
| **Comportamiento** | Si está cerrada, se lanza `BusinessRuleViolation` |
| **Clase responsable** | `Solicitud` |
| **Método** | `asegurarNoCerrada()` |

**Código relevante:**
```java
// En Solicitud.java
private void asegurarNoCerrada() {
    if (estado == EstadoSolicitud.CERRADA)
        throw new BusinessRuleViolation("Una solicitud CERRADA no puede modificarse");
}
```

---

### Regla 7: Registro de Historial (Auditoría)

| Aspecto | Detalle |
|---------|----------|
| **Acción que regula** | Todas las acciones relevantes |
| **Condición** | Cada acción debe generar una entrada en el historial |
| **Comportamiento** | Se crea un `EntradaHistorial` con fecha, acción, usuario y observación |
| **Clases responsables** | `Solicitud`, `EntradaHistorial` |
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
    CERRAR_SOLICITUD,
    // Para IA (futuro)
    SUGERENCIA_IA_GENERADA,
    SUGERENCIA_IA_CONFIRMADA,
    SUGERENCIA_IA_AJUSTADA,
    RESUMEN_IA_GENERADO
}

// En Solicitud.java - registrarHistorial()
private void registrarHistorial(AccionHistorial accion, UsuarioReferencia usuario, String observacion) {
    historial.add(new EntradaHistorial(
        UUID.randomUUID(),
        Instant.now(),
        accion,
        usuario,
        observacion
    ));
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

- **Entidades:** `Usuario`, `Solicitud`, `EntradaHistorial`
- **Value Objects:** `Email`, `IdentificacionUsuario`, `UsuarioReferencia`, `DescripcionSolicitud`, `JustificacionPrioridad`, `SolicitudId`
- **Domain Service:** `SolicitudDomainService` (Sin anotaciones Spring)
- **Excepciones:** `DomainException`, `BusinessRuleViolation`
- **Config:** `DomainServiceConfig`

### 4.2 Inyección de Dependencias

El Domain Service se inyecta desde la capa de aplicación mediante configuración:

```java
// DomainServiceConfig.java
@Configuration
public class DomainServiceConfig {
    @Bean
    public SolicitudDomainService solicitudDomainService() {
        return new SolicitudDomainService();
    }
}
```

### 4.3 Value Objects Inmutables

Todos los VO son `record` con validación en el constructor:

```java
// Ejemplo: Email.java
public record Email(String value) {
    public Email {
        if (value == null || value.isBlank()) {
            throw new DomainException("El email es obligatorio");
        }
        if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
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
| `Usuario` | Representa un usuario del sistema con rol (ESTUDIANTE, DOCENTE, COORDINADOR) |
| `Solicitud` | Raíz del agregado, gestiona el ciclo de vida de una solicitud |
| `EntradaHistorial` | Representa una entrada de auditoría en el historial |

### 5.2 Value Objects

| VO | Descripción |
|----|-------------|
| `Email` | Correo electrónico validado |
| `IdentificacionUsuario` | UUID tipado para identificar usuarios |
| `UsuarioReferencia` | Referencia (UUID + nombre) a un usuario |
| `DescripcionSolicitud` | Descripción de la solicitud (10-1000 caracteres) |
| `JustificacionPrioridad` | Justificación de prioridad (mínimo 10 caracteres) |
| `SolicitudId` | UUID tipado para identificar solicitudes |
| `AccionHistorial` | Enum con las acciones registrables en el historial |

### 5.3 Excepciones del Dominio

| Excepción | Uso |
|-----------|-----|
| `DomainException` | Excepción base para errores de dominio |
| `BusinessRuleViolation` | Para violaciones de reglas de negocio |

### 5.4 Enums

| Enums             | Contenido                                                                                                                                                                                                                                                                                           |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `AccionHistorial` | REGISTRAR_SOLICITUD, CLASIFICAR_SOLICITUD, PRIORIZAR_SOLICITUD, ASIGNAR_RESPONSABLE, MARCAR_ATENDIDA,CERRAR_SOLICITUD, se genera además de eso unos posibles enums para el uso de IA, los cuales son: SUGERENCIA_IA_GENERADA, SUGERENCIA_IA_CONFIRMADA, SUGERENCIA_IA_AJUSTADA, RESUMEN_IA_GENERADO |
| `CanalOrigen`     | CSU, CORREO, SAC, TELEFONICO, PRESENCIAL                                                                                                                                                                                                                                                            |
| `EstadoSolicitud` | REGISTRADA, CLASIFICADA, EN_ATENCION, ATENDIDA, CERRADA                                                                                                                                                                                                                                             |
| `Prioridad`       | ALTA, MEDIA,                                                                                                                                                                                                                                                                                        |
| `Rol`             | ESTUDIANTE, COORDINADOR, DOCENTE                                                                                                                                                                                                                                                                    |
| `TipoSolicitud`   | REGISTRO_ASIGNATURAS,HOMOLOGACION, CANCELACION_ASIGNATURAS, SOLICITUD_CUPOS, CONSULTA_ACADEMICA                                                                                                                                                                                                     |


---

## 6. Pruebas Unitarias del Dominio

El dominio cuenta con **18 pruebas unitarias** que validan las reglas de negocio:

### UsuarioTest (5 pruebas)
- ✅ crearUsuario_valido
- ✅ crearUsuario_nombreNulo
- ✅ crearUsuario_nombreBlanco
- ✅ crearUsuario_rolNulo
- ✅ crearUsuario_inactivo

### SolicitudTest (13 pruebas)
- ✅ crearSolicitud_valida
- ✅ crearSolicitud_solicitanteNulo
- ✅ crearSolicitud_canalNulo
- ✅ clasificar_enEstadoRegistrada
- ✅ clasificar_estadoIncorrecto
- ✅ priorizar_enEstadoClasificada
- ✅ asignarResponsable_estadoClasificada
- ✅ asignarResponsable_usuarioInactivo
- ✅ asignarResponsable_estadoIncorrecto
- ✅ marcarAtendida_estadoEnAtencion
- ✅ cerrar_estadoAtendida
- ✅ cerrar_estadoIncorrecto
- ✅ estadoInicial_registrada

---

## 7. Notas de Implementación

### 7.1 Decisiones de Diseño

1. **Dominio Puro:** El dominio no tiene dependencias de frameworks. Esto facilita las pruebas unitarias y la portabilidad.

2. **Value Objects como Records:** Usamos `record` de Java para VOs inmutables con validación automática.

3. **Enum para Acciones de Historial:** En lugar de strings, usamos un enum tipado para las acciones del historial.

4. **Domain Service como POJO:** El servicio de dominio no tiene anotaciones de Spring, se inyecta desde la configuración.

### 7.2 Posibles Extensiones

- **Identante:** La clase IdentificacionSolicitud está actualmente inactiva. Fue diseñada para soportar solicitantes externos (personas sin cuenta en el sistema). Se puede activar cuando se implemente esta funcionalidad.

---

## 8. Referencias al Código

| Regla | Clase | Método |
|-------|-------|--------|
| Regla 1 | `SolicitudDomainService` | `validarCrearSolicitud` |
| Regla 2 | `SolicitudDomainService` | `validarAsignarResponsable` |
| Regla 3 | `SolicitudDomainService`, `Solicitud` | `validarCrearSolicitud`, `asignarResponsable` |
| Regla 4 | `SolicitudDomainService` | `validarAsignarResponsable` |
| Regla 5 | `Solicitud` | `clasificar`, `priorizar`, `asignarResponsable`, `marcarAtendida`, `cerrar` |
| Regla 6 | `Solicitud` | `asegurarNoCerrada` |
| Regla 7 | `Solicitud`, `EntradaHistorial` | `registrarHistorial`, constructor de `EntradaHistorial` |

---

## 9. Historial de Cambios

| Fecha | Versión | Descripción |
|-------|---------|-------------|
| Marzo 2026 | 1.0 | Versión inicial del documento |

---

*Documento generado para la entrega #1 del proyecto de Programación Avanzada - Universidad del Quindío*
