#!/bin/bash
set -e  # エラー時に停止

cd $(dirname $0)

USERNAME=$(oc whoami)
PROJECT_NAME="${USERNAME}-wk"
APP_NAME=coolstore-quarkus

echo "======================================"
echo "🚀 03-DEPLOY: ユーザー用のデプロイを開始: ${USERNAME}"
echo "📂 プロジェクト: ${PROJECT_NAME}"
echo "🏷️  アプリケーション: ${APP_NAME}"
echo "======================================"

# Switch to the correct project
echo "プロジェクトに切り替え中: ${PROJECT_NAME}"
oc project ${PROJECT_NAME}

# Create or update application
echo "アプリケーションを作成/更新中..."
oc new-app ${APP_NAME} --dry-run -o yaml | oc apply -f -

# Set environment variables from postgres-creds secret
echo "データベース環境変数を設定中..."
oc set env --from=secret/postgres-creds deployment/${APP_NAME}

# Create HTTPS route (edge termination for security)
echo "セキュアルートを作成中..."
if ! oc get route ${APP_NAME} >/dev/null 2>&1; then
    oc create route edge ${APP_NAME} \
      --service=${APP_NAME} \
      --port=8080-tcp
    echo "ルートが正常に作成されました"
else
    echo "ルート ${APP_NAME} は既に存在します"
fi

echo "======================================"
echo "デプロイが正常に完了しました！"
echo "======================================"
echo "デプロイメント概要:"
echo "   ユーザー: ${USERNAME}"
echo "   プロジェクト: ${PROJECT_NAME}"
echo "   アプリケーション: ${APP_NAME}"
echo "   PostgreSQLクラスター: postgres"
echo "   データベースサービス: postgres-rw"ß
echo ""
echo "アプリケーションURL:"
ROUTE_URL=$(oc get route ${APP_NAME} -o jsonpath='{.spec.host}' 2>/dev/null || echo "Route not ready yet")
if [ "$ROUTE_URL" != "Route not ready yet" ]; then
    echo "   https://${ROUTE_URL}"
else
    echo "   ルートを作成中..."
    echo "   ステータス確認: oc get route ${APP_NAME}"
fi
echo ""
echo "便利なコマンド:"
echo "   oc project ${PROJECT_NAME}  # プロジェクトに切り替え"
echo "   oc get all                  # 全リソースを表示"
echo "   oc get cluster              # PostgreSQLクラスターを表示"
echo "   oc logs deployment/${APP_NAME} -f  # アプリケーションログを表示"
echo "======================================" 