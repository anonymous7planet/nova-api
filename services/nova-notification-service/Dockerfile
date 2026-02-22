FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG JAR_FILE=services/nova-notification-service/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 9001