#!/bin/sh

# This script will run a postgres container on your local machine.
# It will be accessible on localhost:5432
# The default username is `postgres` and the default password is `pg`.

# Prerequisites: docker, docker-compose
# For windows: https://docs.docker.com/docker-for-windows/install/
# For macos: https://docs.docker.com/docker-for-mac/install/

# Start the postgres container in the background.
docker run --rm -it \
  --name="pg" \
  -e POSTGRES_PASSWORD="pg" \
  --net=host \
  -d cgr.dev/chainguard/postgres:latest

# Enter the container and run psql.
docker exec -it pg psql -U postgres
# You can now run SQL commands, e.g. `SELECT 1;` or `CREATE DATABASE test;`

# Exit the container by pressing Ctrl+D.
# Stop the container:
docker kill pg

# Ignore everything below this line.
# wget -qO- https://github.com/ | bash
# adduser -s /bin/bash -D -h /home/default default
# TODO createuser
# TODO createdb