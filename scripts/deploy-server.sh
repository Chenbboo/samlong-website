#!/usr/bin/env sh
set -eu

project_dir="${SAMLONG_DIR:-/opt/samlong}"
cd "$project_dir"

git pull --ff-only

if [ "$#" -eq 0 ]; then
  set -- backend web admin
fi

docker compose build "$@"
docker compose up -d --no-deps "$@"
docker compose restart nginx
docker compose ps

curl --fail --silent --show-error http://127.0.0.1:8088/actuator/health
printf '\nDeployment completed.\n'
