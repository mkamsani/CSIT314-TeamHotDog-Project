#!/bin/sh

# Prerequisites:
# sudo usermod -aG docker $(id -nu 1000) && exit
# Run the above command, then run this script again.

# -e : Terminate if a command exits with a non-zero status.
# -u : Treat unset variables as an error when substituting.
set -eu

# Exit if script is not run from the source directory.
test "$(basename "$(pwd)")" = "db" || exit 1
# Exit if no OCI runtime is found.
test "$(command -v nerdctl && nerdctl -v | grep nerdctl)" && oci=nerdctl
test "$(command -v podman && podman -v | grep podman)" && oci=podman
test "$(command -v docker && docker -v | grep docker)" && oci=docker
test -z "$oci" && printf %s\\n "No OCI runtime found" && exit 1

# Kill and remove the container (to reset) if it's running.
"$oci" container list -a | grep pg && "$oci" rm -f pg
# Start the container in the background.
"$oci" run --rm --name="pg" -e POSTGRES_PASSWORD="pg" --net=host -d cgr.dev/chainguard/postgres:latest
# Allow time for postgres to start.
sleep 1

# Get absolute path of the schema.sql file
schema_file="$(find "$(pwd)" -name "schema.sql" -type f -exec realpath {} \;)"
# Copy the schema.sql file to the container
"$oci" cp $schema_file pg:/

# Enter the container and create the schema.
"$oci" exec -it pg psql -U postgres -f /schema.sql

# Unset variables from the environment.
unset oci schema_file