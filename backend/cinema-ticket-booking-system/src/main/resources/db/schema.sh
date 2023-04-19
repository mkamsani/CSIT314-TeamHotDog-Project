#!/bin/sh
test "$(which -v podman)" && oci=podman
test "$(which -v docker)" && oci=docker
test -z "$oci" && echo "No OCI runtime found" && exit 1
# Start the postgres container in the background.
docker run --rm --name="pg" -e POSTGRES_PASSWORD="pg" --net=host -d cgr.dev/chainguard/postgres:latest
# docker kill pg