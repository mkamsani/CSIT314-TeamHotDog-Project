#!/bin/sh
set -eux
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
"$oci" run --rm -d --net=host      \
-u=postgres                        \
--security-opt no-new-privileges   \
--name="pg"                        \
-e POSTGRES_PASSWORD="pg"          \
cgr.dev/chainguard/postgres:latest
if test "$(hostname)" = "fedora"; then
sleep 1 # Allow time for postgres to start.
else
sleep 5 # Allow more time for postgres on WSL2 to start.
fi

# Copy the schema.sql file to the container
"$oci" cp "$(find "$(pwd)" -name "schema.sql"    -type f -exec realpath {} \;)" pg:/home/postgres
"$oci" cp "$(find "$(pwd)" -name "trigger.sql"   -type f -exec realpath {} \;)" pg:/home/postgres
"$oci" cp "$(find "$(pwd)" -name "data_base.sql" -type f -exec realpath {} \;)" pg:/home/postgres
"$oci" cp "$(find "$(pwd)" -name "data_many.sql" -type f -exec realpath {} \;)" pg:/home/postgres
# Enter the container and create the schema.
"$oci" exec pg psql -U postgres -f /home/postgres/schema.sql
"$oci" exec pg psql -U postgres -f /home/postgres/trigger.sql
"$oci" exec pg psql -U postgres -f /home/postgres/data_base.sql
# (Optionally) insert large amounts of data.
"$oci" exec pg psql -U postgres -f /home/postgres/data_many.sql

# Unset variables from the environment.
unset oci