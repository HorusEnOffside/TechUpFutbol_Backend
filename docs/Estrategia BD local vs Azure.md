## Estrategia de Base de Datos: Local vs Azure

### Local (Desarrollo)

Para correr el proyecto localmente, la base de datos corre en un contenedor Docker o en una instalación local de PostgreSQL.

Configura tu `.env` así:

```env
DB_URL=jdbc:postgresql://localhost:5432/techcup
POSTGRES_USER=techcup_owner
POSTGRES_PASSWORD=techcup123
POSTGRES_DB=techcup
```

Si usas Docker Compose, cambia `localhost` por `db`:

```env
DB_URL=jdbc:postgresql://db:5432/techcup
```
 
---

### Azure (Producción)

En producción se usa **Azure Database for PostgreSQL**. La cadena de conexión cambia así:

```env
DB_URL=jdbc:postgresql://<servidor>.postgres.database.azure.com:5432/techcup?sslmode=require
POSTGRES_USER=techcup_owner@<servidor>
POSTGRES_PASSWORD=<contraseña-segura>
POSTGRES_DB=techcup
```

> El parámetro `sslmode=require` es obligatorio en Azure.
 
---

### Resumen

| | Local | Azure |
|---|---|---|
| Host | `localhost` o `db` | `<servidor>.postgres.database.azure.com` |
| Puerto | `5432` | `5432` |
| SSL | No requerido | Obligatorio (`sslmode=require`) |
| Usuario | `techcup_owner` | `techcup_owner@<servidor>` |
| Configuración | `.env` local | Variables de entorno en el servidor |