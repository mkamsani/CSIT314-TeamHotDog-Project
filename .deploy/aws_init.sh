#!/bin/sh

#
# This script is intended to be run on a fresh Ubuntu 20.04 LTS instance
# to install and configure the necessary software to run the My Webapp.
# It should not be executed by a GitHub runner.
#

# Install the necessary software.
sudo apt update -y && sudo apt upgrade -y
sudo apt install -y nginx \
                    php php-fpm php-curl php-gd php-json php-zip libapache2-mod-php \
                    openjdk-17-jdk openjdk-17-jre-headless default-jre \
                    wget ca-certificates

# TODO check if there's some intermediate steps
wget -qO- https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
sudo apt update -y && sudo apt upgrade -y
sudo apt install -y postgresql postgresql-contrib
sudo service postgresql status
# TODO check if there's some intermediate steps
sudo systemctl enable --now nginx.service
sudo systemctl enable --now postgresql.service

# Create the necessary folders.
mkdir -p ~/bin ~/ctbs

# Replace the default nginx configuration with a new one.
printf "%s\n" 'server {'                                                            > /etc/nginx/sites-available/default
printf "%s\n" '           listen 80 default_server;'                               >> /etc/nginx/sites-available/default
printf "%s\n" '           listen [::]:80 default_server;'                          >> /etc/nginx/sites-available/default
printf "%s\n" '           root /var/www/html;'                                     >> /etc/nginx/sites-available/default
printf "%s\n" '           index.php index.html index.htm index.nginx-debian.html;' >> /etc/nginx/sites-available/default
printf "%s\n" '           server_name _;'                                          >> /etc/nginx/sites-available/default
printf "%s\n" '           location / {'                                            >> /etc/nginx/sites-available/default
printf "%s\n" '           try_files $uri $uri/ =404;'                              >> /etc/nginx/sites-available/default
printf "%s\n" '           }'                                                       >> /etc/nginx/sites-available/default
printf "%s\n" '           location ~ \.php$ {'                                     >> /etc/nginx/sites-available/default
printf "%s\n" '           include snippets/fastcgi-php.conf;'                      >> /etc/nginx/sites-available/default
printf "%s\n" '           fastcgi_pass unix:/var/run/php/php7.4-fpm.sock;'         >> /etc/nginx/sites-available/default
printf "%s\n" '           }'                                                       >> /etc/nginx/sites-available/default
printf "%s\n" '}'                                                                  >> /etc/nginx/sites-available/default

# Create a service "spring-boot-app" that will run the backend application.
sudo tee /etc/systemd/system/spring-boot-app.service <<EOF
[Unit]
Description=My SpringBootApp Java REST Service
[Service]
User=ubuntu

# The configuration file application.properties should be here:
#change this to your workspace
WorkingDirectory=/home/ubuntu/ctbs

#Path to executable, points straight to the jar file
ExecStart=/usr/bin/java -jar backend-1.0.0-SNAPSHOT.jar

SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target

EOF

sudo systemctl daemon-reload
sudo systemctl enable --now spring-boot-app
sudo systemctl status spring-boot-app
# List all enabled systemd services.
# systemctl list-unit-files --state=enabled