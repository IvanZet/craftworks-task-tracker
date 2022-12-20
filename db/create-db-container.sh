#!/usr/bin/env bash
docker container create \
  --name craftworks-db \
  -e POSTGRES_PASSWORD=sample \
  -e POSTGRES_USER=sample \
  -e POSTGRES_DB=sample \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  -p 127.0.0.1:5432:5432 \
  postgres:15.1
