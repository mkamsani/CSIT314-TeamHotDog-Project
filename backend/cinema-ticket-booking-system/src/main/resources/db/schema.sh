#!/bin/sh

# This script will run a postgres container on your local machine.
# It will be accessible on localhost:5432
# The default username is `postgres` and the default password is `pg`.

# Prerequisites: docker, docker-compose
# For windows: https://docs.docker.com/docker-for-windows/install/
# For macos: https://docs.docker.com/docker-for-mac/install/

if ! command -v docker > /dev/null; then
echo hello;
fi

if test ! $(which docker || which podman); then
cat << EOF
A container runtime is required to run this script.
For windows: https://docs.docker.com/docker-for-windows/install/
For macos: https://docs.docker.com/docker-for-mac/install/
EOF
exit 1
fi

# Start the postgres container in the background.

docker run --rm --name="pg" -e POSTGRES_PASSWORD="pg" --net=host -d cgr.dev/chainguard/postgres:latest
docker run --rm --name="pg_map" -e POSTGRES_PASSWORD="pg" -p 5432:5432 -d cgr.dev/chainguard/postgres:latest

# Enter the container and run psql.
docker exec -it pg psql -U psql -U postgres -f ~/path/to/file.sql
# You can now run SQL commands, e.g. `SELECT 1;` or `CREATE DATABASE test;`
# source a file

# Exit the container by pressing Ctrl+D.
# Stop the container:
# docker kill pg