#!/bin/sh

#
# This script is intended to be run on a fresh Ubuntu 20.04 LTS instance
# to install and configure the necessary software to run the My Webapp.
# It should not be executed by a GitHub runner.
#

sudo apt update -y
sudo apt upgrade -y
sudo apt install -y nginx php php-fpm postgresql libapache2-mod-php php-curl php-gd php-json php-zip
# TODO: install java
sudo apt install -y openjdk-17-jdk
sudo systemctl enable --now nginx.service
sudo systemctl enable --now postgresql.service

tee /etc/systemd/system/my-webapp.service <<EOF
[Unit]
Description=My Webapp Java REST Service
[Service]
User=ubuntu
# The configuration file application.properties should be here:

#change this to your workspace
WorkingDirectory=/home/ubuntu/workspace

#path to executable.
#executable is a bash script which calls jar file
ExecStart=/home/ubuntu/bin/spring-boot-app

SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

# Create a Bash Script to Call Your Service
sudo tee ~/bin/my-webapp <<EOF
#!/bin/sh
sudo /usr/bin/java -jar spring-boot-app-1.0-SNAPSHOT.jar server config.yml
EOF
sudo systemctl daemon-reload
sudo systemctl enable my-webapp.service
sudo systemctl start my-webapp
sudo systemctl status my-webapp
