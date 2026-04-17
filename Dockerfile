# ================================
# ETAPA 1: Build
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar dependencias primero (mejor uso de caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn package -Dmaven.test.skip=true -q

# ================================
# ETAPA 2: Runtime
# ================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S techcup && adduser -S techcup -G techcup

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Crear carpeta de logs
RUN mkdir -p logs && chown -R techcup:techcup /app

USER techcup

# Variables de entorno con valores por defecto
ENV HTTPS_PORT=8443 \
    SSL_ENABLED=true \
    SSL_KEY_STORE=classpath:keystore.p12 \
    SSL_KEY_STORE_PASSWORD=password123 \
    SSL_KEY_ALIAS=TechcupFutbol \
    SPRING_SECURITY_USER_NAME=admin \
    SPRING_SECURITY_USER_PASSWORD=123 \
    DB_URL=jdbc:postgresql://db:5432/techcup \
    JWT_SECRET=Qw8vZ!2pLk#7sTn@4rXc9eBz1YhGfUjM \
    JWT_EXPIRATION=3600000

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "app.jar"]