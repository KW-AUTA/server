# 빌드 단계
FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

# Gradle 설정 파일 복사
COPY settings.gradle gradlew gradlew.bat ./
COPY gradle gradle

# 소스 코드 복사
COPY . .

# Gradle 빌드
RUN chmod +x gradlew

RUN ./gradlew build -x test -x asciidoctor --no-daemon

# 실행 단계
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
