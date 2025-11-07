# 빌드 단계
FROM gradle:8.13-jdk17 AS build

WORKDIR /app

# Gradle 설정 파일 복사
COPY settings.gradle gradlew gradlew.bat ./
COPY gradle gradle

# 소스 코드 복사
COPY . .

# Gradle 빌드
RUN chmod +x gradlew

RUN ./gradlew clean test asciidoctor bootJar --no-daemon

# 실행 단계
#FROM openjdk:17-jdk-slim
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
