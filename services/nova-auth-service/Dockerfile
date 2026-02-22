FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# 빌드 시점에 생성된 jar 위치 지정
ARG JAR_FILE=services/nova-auth-service/build/libs/*.jar
COPY ${JAR_FILE} app.jar
# 프로젝트 노바 보안 전략: Gateway IP만 허용하도록 나중에 설정 추가 필요
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 9002