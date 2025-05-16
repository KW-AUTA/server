set -e

SERVER_USER="ubuntu"
SERVER_IP="219.255.242.174"
SERVER_DIR="~"
PEM_KEY_PATH="./auta.pem"

echo "[1] Asciidoctor 문서 빌드 시작..."
./gradlew asciidoctor
echo "[1] 완료 ✅"

echo "[2] Gradle 빌드(JAR 생성) 시작..."
./gradlew clean bootJar -Dspring.profiles.active=prod
echo "[2] 완료 ✅"

BUILD_DIR="build/libs"
TARGET_FILE=$(ls $BUILD_DIR/*.jar)

echo "[3] 빌드된 JAR 파일 확인: $TARGET_FILE"

echo "[4] 서버로 파일 전송 시작..."
scp "$TARGET_FILE" "$SERVER_USER@$SERVER_IP:$SERVER_DIR"
echo "[4] 전송 완료 ✅"

echo "🎉 모든 작업 완료!"