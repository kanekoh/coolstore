#!/bin/bash
set -e  # エラー時に停止

cd $(dirname $0)

USERNAME=$(oc whoami)
PROJECT_NAME="${USERNAME}-wk"
APP_NAME=coolstore-quarkus

echo "======================================"
echo "🔨 02-BUILD: ユーザー用のビルドを開始: ${USERNAME}"
echo "📂 プロジェクト: ${PROJECT_NAME}"
echo "🏷️  アプリケーション: ${APP_NAME}"
echo "======================================"

# Switch to the correct project
echo "プロジェクトに切り替え中: ${PROJECT_NAME}"
oc project ${PROJECT_NAME}

# Build application as uber-jar
echo "Mavenでアプリケーションをビルド中..."
mvn clean package -DskipTests=true -Dquarkus.package.type=uber-jar

# Get the built jar file (more specific pattern for uber-jar)
JAR=$(ls -1 target/*-runner.jar)
echo "使用するJAR: $JAR"

# Start OpenShift build
echo "OpenShiftビルドを開始中..."
oc start-build ${APP_NAME} --from-file=$JAR --follow

echo "✅ ビルドが正常に完了しました！"
