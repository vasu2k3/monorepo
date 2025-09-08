#!/usr/bin/env bash
set -euo pipefail
NS=orders
TAG="dev-$(date +%s)"

echo "🔧 Building & pushing backend..."
docker build -t localhost:5500/orders-service:$TAG ./backend
docker push localhost:5500/orders-service:$TAG

echo "🎨 Building & pushing frontend..."
# .env should point to http://orders.local/api/v1
docker build -t localhost:5500/orders-frontend:dev ./frontend
docker push localhost:5500/orders-frontend:dev

echo "🚀 Helm upgrade backend (and MySQL)..."
helm upgrade --install orders ./infra/helm/orders-service \
  -n $NS --create-namespace \
  --set image.repository=localhost:5500/orders-service \
  --set image.tag=$TAG

echo "🖥️  Helm upgrade frontend..."
helm upgrade --install orders-ui ./infra/helm/frontend -n $NS

echo "⏳ Waiting for rollouts..."
kubectl -n $NS rollout status deploy/orders-orders-service --timeout=180s
kubectl -n $NS rollout status deploy/orders-ui-orders-frontend --timeout=180s

echo "✅ Done. Open: http://orders.local"
