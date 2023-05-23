#!/bin/sh

# This script is used to create a container with Nginx and PHP.
# Nginx will listen on port 8080 and PHP will be configured to use it.
# Spring Boot will be accessed via Nginx on port 8080.

# Prerequisites:
# sudo usermod -aG docker $(id -nu 1000) && exit
# Run the above command, then run this script again.

# Exit if script is not run from the source directory.
test "$(basename "$(pwd)")" = "development-files" || exit 1
# Exit if no OCI runtime is found.
test "$(command -v nerdctl && nerdctl -v | grep nerdctl)" && oci=nerdctl
test "$(command -v podman && podman -v   | grep podman)"  && oci=podman
test "$(command -v docker && docker -v   | grep docker)"  && oci=docker
test -z "$oci" && printf %s\\n "No OCI runtime found" && exit 1

# Kill and remove the container (to reset) if it's running.
sleep 1 && "$oci" container list -a | grep php_apache && "$oci" rm -f php_apache

"$oci" run --net=host --security-opt label=disable --name=php_apache -d \
-v "$(pwd)/../frontend":/var/www/html:rw \
-v "$(pwd)"/nginx_php.conf:/etc/nginx/conf.d/default.conf \
trafex/php-nginx:latest

unset oci