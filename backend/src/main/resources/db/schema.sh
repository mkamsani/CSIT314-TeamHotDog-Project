#!/bin/sh -eux
# -eux means:
# -e : Terminate if a command exits with a non-zero status.
# -u : Treat unset variables as an error when substituting.
# -x : Print commands and their arguments as they are executed.

# Prerequisites:
# sudo usermod -aG docker $(id -nu 1000) && exit
# Run the above command, then run this script again.

# Exit if script is not run from the source directory.
test "$(basename "$(pwd)")" = "db" || exit 1
# Exit if no OCI runtime is found.
test "$(command -v nerdctl && nerdctl -v | grep nerdctl)" && oci=nerdctl
test "$(command -v podman && podman -v   | grep podman)"  && oci=podman
test "$(command -v docker && docker -v   | grep docker)"  && oci=docker
test -z "$oci" && printf %s\\n "No OCI runtime found" && exit 1

# Unset set -e to allow the script to continue if the container is not running.
set +e
# Kill and remove the container (to reset) if it's running.
sleep 2 && "$oci" container list -a | grep pg && "$oci" rm -f pg

# Start the container in the background.
# --rm                    : Automatically remove the container when it exits.
# -d                      : Run container in the background and print container ID.
# --net=host              : Use the host's network stack.
# -u=postgres             : https://medium.com/nttlabs/dont-use-host-network-namespace-f548aeeef575
# --security-opt          : https://docs.podman.io/en/latest/markdown/podman-run.1.html#security-opt-option
# --name                  : Assign a name to the container.
# -e                      : Set environment variables.
# -e POSTGRES_PASSWORD    : Set the password for the default user.
# -e POSTGRES_INITDB_ARGS : See https://www.postgresql.org/docs/current/app-initdb.html
# -e TZ                   : Set the timezone.
# Reference:
# https://hub.docker.com/_/postgres
# https://docs.docker.com/engine/reference/commandline/run/
# https://docs.podman.io/en/latest/markdown/podman-run.1.html
# https://github.com/containerd/nerdctl/blob/main/docs/command-reference.md#whale-blue_square-nerdctl-run
"$oci" run --rm -d --net=host            \
-u=postgres                              \
--security-opt no-new-privileges         \
--name="pg"                              \
-e POSTGRES_PASSWORD="pg"                \
cgr.dev/chainguard/postgres:latest
# Allow time for postgres to start.
sleep 3

# Get absolute path of the schema.sql file
schema_file="$(find "$(pwd)" -name "schema.sql" -type f -exec realpath {} \;)"
data_file="$(find "$(pwd)" -name "data.sql" -type f -exec realpath {} \;)"
# Copy the schema.sql file to the container
"$oci" cp $schema_file pg:/home/postgres
"$oci" cp $data_file pg:/home/postgres
# Enter the container and create the schema.
"$oci" exec -it pg psql -U postgres -f /home/postgres/schema.sql
"$oci" exec -it pg psql -U postgres -f /home/postgres/data.sql

# Unset variables from the environment.
unset oci schema_file data_file