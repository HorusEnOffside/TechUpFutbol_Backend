# HORUS EN OFFSIDE
![logoFInal.png](docs/images/logoFInal.png)

## Integrantes

- Andres Felipe Cardozo
- Juan Camilo Cristancho
- Juan David Gómez
- Mariana Malagón
- Sebastian Castillejo 

## TechCup Fútbol

## Enunciado del problema

Actualmente, la organización de torneos estudiantiles de fútbol en la Escuela presenta retos significativos en la inscripción de equipos, registro de jugadores, manejo de pagos y seguimiento de resultados. No existe una plataforma integral que permita la gestión eficiente, segura y automatizada de todo el ciclo del torneo: desde la inscripción hasta la visualización de estadísticas y control disciplinario.

## Índice

1. [Diagrama de contexto del sistema](#diagrama-de-contexto-del-sistema)
2. [Definición de requerimientos](#definición-de-requerimientos)
    - [Funcionales](#requerimientos-funcionales)
    - [No funcionales](#requerimientos-no-funcionales)
3. [Análisis de requerimientos](#análisis-de-requerimientos)
4. [Manual de identidad](#manual-de-identidad)
5. [Jira: Gestión de tareas y funcionalidades](#jira)
6. [Diagramas de secuencia](#diagramas-de-secuencia)
7. [Diagrama de componentes general](#diagrama-de-componentes-general)
8. [Diagrama de componentes especificos](#diagrama-de-componentes-especificos)
9. [Diagrama de clases](#diagrama-de-clases)
---

## Diagrama de contexto del sistema
![DiagramaContextoTechCupFINAL.png](docs/images/DiagramaContextoTechCupFINAL.png)

**Explicación:** Este diagrama delimita el sistema TECHCUP FÚTBOL y muestra cómo interactúan los actores principales con la plataforma. Resume el alcance funcional del proyecto: registro de usuarios, administración de perfiles deportivos, gestión de equipos, partidos, pagos, sanciones y consultas según el rol.

El sistema separa claramente los permisos por tipo de usuario. Los capitanes administran equipos e invitaciones, los organizadores controlan torneos, pagos y resultados, los árbitros consultan los partidos asignados y el administrador conserva capacidades de supervisión y auditoría.

También evidencia la integración con procesos externos como el pago y la carga de comprobantes, lo que permite trazabilidad y validación dentro del flujo de inscripción al torneo.

---

## Definición de requerimientos

### Requerimientos funcionales y Requerimientos no funcionales

#### Documento de definición de requerimientos del sistema, donde se consolidan los requerimientos funcionales y no funcionales que delimitan el alcance del proyecto:
#### https://drive.google.com/file/d/1AOBtzUp4Ludo7ZzYN-Zx059rskGi4_KU/view?usp=sharing

## Análisis de requerimientos

#### Documento de análisis de requerimientos del sistema, donde se desarrollan y justifican las necesidades identificadas para el proyecto:  
#### https://1drv.ms/w/c/7d34c3acd28e130c/IQA1grpq8wYKT5Vyg6x0otSSAUKBbCGLtZLGRaYhcGPXc7Q?e=e3IsW9

---


## Manual de identidad
https://pruebacorreoescuelaingeduco.sharepoint.com/:p:/s/DOSW-2026-1/IQA1oPTMzFDaSpldO_fjkOzEAQrW69M2W3Ogkj8GeMJL3mQ?e=SzFiPT
Documento de manual de identidad visual, donde se definen los lineamientos de marca, colores, uso visual y presentación del proyecto.

---

## Jira

Tablero de Jira del proyecto, donde se organizan las tareas, funcionalidades y seguimiento del trabajo del equipo.
https://dosw-2026-01.atlassian.net/jira/software/projects/SCRUM/boards/1/backlog

---

## Diagrama de componentes general
![Diagrama componentes especificos](docs/images/DiagramaComponentesGeneral.drawio.png)

**Explicación:** Este diagrama presenta la arquitectura general del backend y cómo se organizan sus capas y módulos principales. Permite entender la separación entre responsabilidades de negocio, exposición de servicios, persistencia e integración con componentes externos.

Además, muestra la base estructural sobre la que se implementan los procesos del sistema: autenticación, gestión de usuarios, equipos, partidos, pagos y estadísticas.

## Diagrama de componentes especificos
![Diagrama componentes especificos](docs/images/Diagramacomponentesespecificos.drawio.png)

**Explicación:** Este diagrama detalla la interacción entre los componentes internos del sistema y sus dependencias directas. Se enfoca en cómo los módulos concretos colaboran para ejecutar los casos de uso del dominio deportivo.

Aquí se aprecia mejor la relación entre servicios de negocio, controladores, repositorios y entidades, lo que facilita mantener bajo acoplamiento y alta cohesión dentro de la aplicación.

## Diagrama de clases
![Diagrama de clases](docs/images/DiagramaClases.png)

**Explicación:** Este diagrama representa el modelo de dominio del proyecto y las relaciones entre sus entidades principales. Muestra cómo se conectan usuarios, perfiles deportivos, equipos, invitaciones, torneos, partidos, pagos y sanciones.

Su objetivo es dejar claras las cardinalidades, herencias y asociaciones que soportan las reglas de negocio del sistema, especialmente las restricciones de roles, pertenencia a equipos y seguimiento de resultados.

---

## Diagramas de secuencia

Documento técnico de diagramas de secuencia, donde se agrupan los flujos de interacción del sistema por módulo funcional. Puedes consultarlo aquí:

[Ver documento de diagramas de secuencia](./docs/UML/secuencia/README.md)

## Sustento y justificación técnica

En este repositorio no sólo se encontrará los artefactos básicos, sino también el razonamiento y análisis detrás de cada decisión:
- Cada requerimiento fue discutido y agrupado según las necesidades de bajo acoplamiento, alta cohesión y escalabilidad del sistema.
- Los módulos y funcionalidades fueron definidos tras analizar los flujos críticos del torneo estudiantil.
- El diseño visual y la lógica de roles se fundamentaron en principios de seguridad, usabilidad y la experiencia previa en sistemas similares.
