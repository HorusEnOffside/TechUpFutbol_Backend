# Diagramas de secuencia

Este documento agrupa los diagramas de secuencia del proyecto TechCup Fútbol, organizados por módulo funcional. Cada flujo muestra cómo interactúan los actores con el sistema durante procesos clave como el registro de usuarios, la gestión de equipos y la administración de partidos.

---

## Módulo 1 (Usuarios y perfiles deportivos)

Estos diagramas describen el flujo de creación de usuarios por rol, la construcción del perfil deportivo y los pasos de seguridad asociados al registro.

| Diagrama | Descripción |
|----------|-------------|
| ![createAdminUser](./m1/createAdminUser.png) | Crear usuario administrador | Flujo de registro con privilegios elevados para la gestión total de la plataforma. |
| ![createFamiliarUser](./m1/createFamiliarUser.png) | Crear usuario familiar | Registro de un usuario externo con correo Gmail y acceso a funciones permitidas por su rol. |
| ![createGraduateUser](./m1/createGraduateUser.png) | Crear usuario egresado | Alta de un egresado con validación de correo institucional y asignación de rol correspondiente. |
| ![createOrganizerUser](./m1/createOrganizerUser.png) | Crear usuario organizador | Creación de la cuenta encargada de administrar torneos, pagos y resultados. |
| ![createRefereeUser](./m1/createRefereeUser.png) | Crear usuario árbitro | Registro del árbitro con acceso exclusivo a partidos asignados y su programación. |
| ![createSportsProfileFamiliar](./m1/createSportsProfileFamiliar.png) | Crear perfil deportivo - Familiar | Asociación de datos deportivos al usuario, como posición, dorsal y foto, cuando aplica. |
| ![createSportsProfileGraduate](./m1/createSportsProfileGraduate.png) | Crear perfil deportivo - Egresado | Configuración del perfil deportivo para un egresado dentro del sistema. |
| ![createSportsProfileStudent](./m1/createSportsProfileStudent.png) | Crear perfil deportivo - Estudiante | Registro del perfil deportivo del estudiante para su participación en el torneo. |
| ![createSportsProfileTeacher](./m1/createSportsProfileTeacher.png) | Crear perfil deportivo - Profesor | Flujo de creación del perfil deportivo para un profesor participante. |
| ![createStudentUser](./m1/createStudentUser.png) | Crear usuario estudiante | Registro de un estudiante con credenciales institucionales y rol de jugador. |
| ![createTeacherUser](./m1/createTeacherUser.png) | Crear usuario profesor | Alta de un profesor en la plataforma con su respectiva validación de acceso. |
| ![hashPassword](./m1/hashPassword.png) | Hash de contraseña | Proceso de seguridad para almacenar contraseñas de forma cifrada. |
| ![idGenerator](./m1/idGenerator.png) | Generador de ID | Creación automática de identificadores únicos para usuarios y entidades relacionadas. |

---

## Módulo 3 (Equipos e invitaciones)

Estos diagramas muestran cómo el capitán crea su equipo y cómo se gestionan las invitaciones para incorporar jugadores al plantel.

| Diagrama | Descripción |
|----------|-------------|
| ![createTeam](./m3/createTeam.png) | Crear equipo | Flujo en el que el capitán registra nombre, escudo, colores y valida condiciones básicas del equipo. |
| ![handleInvitation](./m3/handleInvitation.png) | Manejar invitación | Proceso de envío, aceptación o rechazo de invitaciones para unir jugadores al equipo. |

---

## Módulo 7 (Partidos y árbitros)

Estos diagramas reflejan la operación del partido desde la asignación del árbitro hasta el registro final de la información del encuentro.

| Diagrama | Descripción |
|----------|-------------|
| ![asignReferee](./m7/asignReferee.png) | Asignar árbitro | El organizador asigna un árbitro a un partido según disponibilidad y programación. |
| ![mostrarInfoPartidos](./m7/mostrarInfoPartidos.png) | Mostrar información de partidos | Vista del árbitro para consultar fecha, hora y cancha de sus partidos asignados. |
| ![registerMatch](./m7/registerMatch.png) | Registrar partido | Registro del marcador, goleadores, tarjetas y demás eventos del encuentro. |

