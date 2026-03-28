## Uso de PostgreSQL con Docker

1. **Levantar la base de datos con Docker:**

	En terminal
     ```sh
     docker run --name techcup-postgres -e POSTGRES_DB=techcup -e POSTGRES_USER=techcup_owner -e POSTGRES_PASSWORD=techcup123 -p 5432:5432 -d postgres:16
     ```

     - Usuario: `techcup_owner`
     - Contraseña: `techcup123`
     - Base de datos: `techcup`
     - Puerto: `5432`

2. **Verifica que el contenedor esté corriendo:**

     ```sh
     docker ps
     ```

3. **Conexión desde Spring Boot:**

     La configuración en yaml (usa variables de entorno para mayor seguridad):

     ```yaml
     spring:
       datasource:
         url: ${DB_URL:jdbc:postgresql://localhost:5432/techcup}
         username: ${DB_USERNAME:techcup_owner}
         password: ${DB_PASSWORD:techcup123}
         driver-class-name: org.postgresql.Driver
       jpa:
         hibernate:
           ddl-auto: update
         show-sql: true
         properties:
           hibernate:
             format_sql: true
         database-platform: org.hibernate.dialect.PostgreSQLDialect
     ```

4. **Inicia tu aplicación Spring Boot** y verifica que se conecte correctamente a la base de datos.

Para iniciar el contenedor si ya existe:

```sh
docker start techcup-postgres
```

5. **Acceder a la base de datos desde consola:**

```sh
docker exec -it techcup-postgres psql -U techcup_owner -d techcup
```

Comandos útiles en psql:

- `\dt` — listar tablas
- `\d <tabla>` — describir tabla
- `SELECT * FROM <tabla> LIMIT 10;` — ver datos

6. **Reiniciar y eliminar el contenedor:**

```sh
docker stop techcup-postgres
docker rm techcup-postgres
```