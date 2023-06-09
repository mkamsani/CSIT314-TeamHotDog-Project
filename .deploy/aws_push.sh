#!/bin/sh

#
# This script is executed on the server,
# each time a new version of the application is deployed.
#

# Create bin folder if it does not exist.
test -d ~/bin || mkdir ~/bin

# Move backend application to bin folder.
if test -f ~/bin/backend-1.0.0-SNAPSHOT.jar; then
rm -f ~/bin/backend-1.0.0-SNAPSHOT.jar
cp -f ~/ctbs/backend-1.0.0-SNAPSHOT.jar ~/bin
rm -f ~/ctbs/backend-1.0.0-SNAPSHOT.jar
else
printf "%s: %s\n" "No backend-1.0.0-SNAPSHOT.jar file found" "$(date)" >> ~/ctbs/log.txt
fi

# Move frontend files to nginx folder.
if test -d ~/ctbs/frontend; then
sudo rm -rf /var/www/html/*
sudo mv -f ~/ctbs/frontend/* /var/www/html
rmdir ~/ctbs/frontend
else
printf "%s: %s\n" "No frontend folder found" "$(date)" >> ~/ctbs/log.txt
fi

sudo systemctl restart spring-boot-app
exit 0
