#!/bin/sh
test "$(nerdctl -v | grep nerdctl)" && oci=nerdctl
test "$(podman -v | grep podman)" && oci=podman
test "$(docker -v | grep docker)" && oci=docker
test -z "$oci" && echo "No OCI runtime found" && exit 1
# Kill the container (to reset) if it's running.
"$oci" container list -a | grep pg && "$oci" rm -f pg
# Start the postgres container in the background.
"$oci" run --rm --name="pg" -e POSTGRES_PASSWORD="pg" --net=host -d cgr.dev/chainguard/postgres:latest
unset "$oci"