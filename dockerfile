FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

RUN adduser -D app
RUN mkdir -p /app/uploads /app/logs \
    && chown -R app:app /app \
    && chmod -R 755 /app \
    && chmod -R 755 /app/uploads /app/logs

COPY --from=builder --chown=app:app /workspace/target/*.jar /app/app.jar

USER app

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]