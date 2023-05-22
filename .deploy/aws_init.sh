#!/bin/sh

#
# This script is intended to be run on a fresh Ubuntu 20.04 LTS instance
# to install and configure the necessary software to run the My Webapp.
# It should not be executed by a GitHub runner.
#

# Install the necessary software
sudo apt update -y
sudo apt upgrade -y
sudo apt install -y nginx php php-fpm postgresql libapache2-mod-php php-curl php-gd php-json php-zip
sudo apt install -y openjdk-17-jdk openjdk-17-jre-headless default-jre wget ca-certificates
# TODO check if there's some intermediate steps
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
sudo apt update
apt install postgresql postgresql-contrib
sudo apt install postgresql postgresql-contrib
service postgresql status
sudo -u postgres psql
# TODO check if there's some intermediate steps
sudo systemctl enable --now nginx.service
sudo systemctl enable --now postgresql.service

# Create the necessary folders
mkdir -p ~/bin
mkdir -p ~/ctbs # Cinema Ticket Booking System

# TODO Change /etc/nginx/sites-available/default to allow PHP files.

[Unit]
Description=My SpringBootApp Java REST Service
[Service]
User=ubuntu
# The configuration file application.properties should be here:
#change this to your workspace
WorkingDirectory=/home/ubuntu/ctbs
#path to executable.
#executable is a bash script which calls jar file
ExecStart=/usr/bin/java -jar backend-1.0.0-SNAPSHOT.jar

SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target
# TODO figure out what is server and config.yml
#  server refers to the server class in the backend
#  config.yml is the configuration file for the server

sudo systemctl daemon-reload
sudo systemctl enable --now my-webapp.service
sudo systemctl status my-webapp
# list all enabled systemd services
# systemctl list-unit-files --state=enabled