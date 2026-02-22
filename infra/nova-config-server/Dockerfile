# 1단계: 빌드된 jar 파일을 실행할 Runtime 이미지 준비
FROM eclipse-temurin:21-jre-alpine

# 작업 디렉토리 설정
WORKDIR /app

# JAR_FILE 인자 설정 (빌드 결과물 위치)
ARG JAR_FILE=infra/nova-config-server/build/libs/*.jar

# jar 파일 복사 및 이름 변경
COPY ${JAR_FILE} app.jar

# 컨테이너 실행 명령 (보안을 위해 root 권한이 아닌 사용자로 실행하는 것이 좋지만, 로컬에선 기본 설정 유지)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 주석: Config Server는 8888 포트를 사용함
EXPOSE 8888