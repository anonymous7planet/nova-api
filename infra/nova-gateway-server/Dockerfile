FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG JAR_FILE=infra/nova-gateway-server/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8080