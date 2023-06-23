#!/bin/sh
set -eux
# -e : Terminate if a command exits with a non-zero status.
# -u : Treat unset variables as an error when substituting.
# -x : Print commands and their arguments as they are executed.

# Prerequisites for WSL2:
# 1) sudo usermod -aG docker $(id -nu 1000) && exit
# 2) This file should be saved with Unix line endings (LF).

# Exit if script is not run from the source directory.
test "$(basename "$(pwd)")" = "development-files" || exit 1
# Exit if no OCI runtime is found.
test "$(command -v nerdctl && nerdctl -v | grep nerdctl)" && oci=nerdctl
test "$(command -v podman  && podman  -v | grep podman)"  && oci=podman
test "$(command -v docker  && docker  -v | grep docker)"  && oci=docker
test -z "$oci" && printf %s\\n "No OCI runtime found" && exit 1

# Unset set -e to allow the script to continue if the container is not running.
set +e
# Kill and remove the container (to reset) if it's running.
if test "$("$oci" container list -a | grep pg)"; then
"$oci" kill pg
"$oci" rm -f pg
fi
set -e

# Start the container in the background.
# Reference:
# https://hub.docker.com/_/postgres
# https://docs.docker.com/engine/reference/commandline/run/
# https://docs.podman.io/en/latest/markdown/podman-run.1.html
# https://github.com/containerd/nerdctl/blob/main/docs/command-reference.md#whale-blue_square-nerdctl-run
"$oci" run --rm -d --net=host      \
-u=postgres                        \
--security-opt no-new-privileges   \
--name="pg"                        \
-e POSTGRES_PASSWORD="pg"          \
cgr.dev/chainguard/postgres:latest
sleep 5

# Copy the schema.sql file to the container
"$oci" cp "$(find "$(pwd)/../database" -name "schema.sql"    -type f -exec realpath {} \;)" pg:/home/postgres
"$oci" cp "$(find "$(pwd)/../database" -name "trigger.sql"   -type f -exec realpath {} \;)" pg:/home/postgres
"$oci" cp "$(find "$(pwd)/../database" -name "data_base.sql" -type f -exec realpath {} \;)" pg:/home/postgres
"$oci" cp "$(find "$(pwd)/../database" -name "data_many.sql" -type f -exec realpath {} \;)" pg:/home/postgres
# Enter the container and create the schema.
"$oci" exec pg psql -U postgres -f /home/postgres/schema.sql
"$oci" exec pg psql -U postgres -f /home/postgres/trigger.sql
"$oci" exec pg psql -U postgres -f /home/postgres/data_base.sql
"$oci" exec pg psql -U postgres -f /home/postgres/data_many.sql
# Unset variables from the environment.
unset oci