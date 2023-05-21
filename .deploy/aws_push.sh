#!/bin/sh

#
# This script is executed on the server, each time a new version of the application is deployed.
# It is executed as root.
#

# Stop SpringBoot.
sudo service whatever stop
# Remove old jar.
sudo rm /usr/bin/springboot.jar
# Copy new jar and frontend files.
sudo cp /tmp/file.jar /usr/bin/springboot.jar
sudo cp /tmp/frontend /var/www/html/
# Restart nginx and SpringBoot.
sudo service whatever start
sudo service nginx restart
exit 0