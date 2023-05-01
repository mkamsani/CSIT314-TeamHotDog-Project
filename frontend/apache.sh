#!/bin/sh

# This script is used to create a container with apache and php.
# Apache will listen on port 8000 and php will be configured to use it.
# The container will be named php_apache and will be removed when stopped.

# Prerequisites:
# sudo usermod -aG docker $(id -nu 1000) && exit
# Run the above command, then run this script again.

# Exit if script is not run from the source directory.
test "$(basename "$(pwd)")" = "frontend" || exit 1
# Exit if no OCI runtime is found.
test "$(command -v nerdctl && nerdctl -v | grep nerdctl)" && oci=nerdctl
test "$(command -v podman && podman -v   | grep podman)"  && oci=podman
test "$(command -v docker && docker -v   | grep docker)"  && oci=docker
test -z "$oci" && printf %s\\n "No OCI runtime found" && exit 1

# Kill and remove the container (to reset) if it's running.
sleep 1 && "$oci" container list -a | grep php_apache && "$oci" rm -f php_apache

# There are two options from here:
# 1. Start the container and copy the source files to it.
#    Any changes to the source files will require the container to be restarted.
#    To restart the container, run this script again.
# 2. Mount this folder as a read/write volume in the container, then start it.
#    Any changes to the source files will be reflected in the container.
#    This option results in reduced security, but is more convenient.

#
# In the code below, comment the option you don't want to use.
#

# Option 1:
# "$oci" run --rm -p 8123:80 --name=php_apache -d php:7.4.3-apache && sleep 1 && "$oci" cp ./. php_apache:/var/www/html/

# Option 2:
#"$oci" run --net=host --security-opt label=disable --name=php_apache -d -v "$(pwd)":/var/www/html:rw -v "$(pwd)"/./apache2.conf:/etc/apache2/apache2.conf:rw php:7.4.3-apache
#"$oci" run --net=host --security-opt label=disable --name=php_apache -d -v "$(pwd)":/var/www/html:rw php:8.2.6RC1-apache
#"$oci" run --net=host --security-opt label=disable --name=php_apache -d -v "$(pwd)":/var/www/html:rw trafex/php-nginx:latest
# take the container's /etc/nginx/conf.d/default.conf
#"$oci" cp php_apache:/etc/nginx/conf.d/default.conf ./nginx_php.conf

"$oci" run --net=host --security-opt label=disable --name=php_apache -d \
-v "$(pwd)":/var/www/html:rw \
-v "$(pwd)"/nginx_php.conf:/etc/nginx/conf.d/default.conf \
trafex/php-nginx:latest

unset oci