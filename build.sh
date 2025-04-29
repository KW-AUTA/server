#!/bin/bash

set -e



SERVER_USER="ubuntu"
SERVER_IP="54.180.85.225"
SERVER_DIR="~"
PEM_KEY_PATH="./auta.pem"

echo "📄 1. Asciidoctor 문서 빌드 시작..."

# asciidoctor 문서 빌드
./gradlew asciidoctor

echo " Asciidoctor 문서 빌드 완료."

echo "2. Gradle 빌드(JAR 생성) 시작..."

# Gradle 빌드 (JAR 만들기)
./gradlew clean bootJar -Dspring.profiles.active=prod

echo "Gradle 빌드 완료."

BUILD_DIR="build/libs"
TARGET_FILE=$(ls $BUILD_DIR/*.jar)

echo "🚀 4. 서버에 SCP로 파일 업로드 시작..."
scp -i "$PEM_KEY_PATH" "$TARGET_FILE" "$SERVER_USER@$SERVER_IP:$SERVER_DIR"

echo "업로드 완료"
echo "모든 작업 완료"