#!/bin/sh

#
# 1) Set up a fresh Ubuntu 20.04 LTS instance.
# 2) Copy database/*.sql files into the instance home directory.
# 3) Run this script.
#

# Install the necessary software.
sudo apt update -y && sudo apt upgrade -y
sudo apt install -y nginx \
                    php php-fpm php-curl php-gd php-json php-zip libapache2-mod-php \
                    openjdk-17-jdk openjdk-17-jre-headless default-jre \
                    wget ca-certificates

wget -qO- https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
sudo apt update -y && sudo apt upgrade -y
sudo apt install -y postgresql postgresql-contrib
sudo service postgresql status
sudo systemctl enable --now nginx.service
sudo systemctl enable --now postgresql.service

# Create the necessary folders.
mkdir -p ~/bin ~/ctbs

# Deploy the database.
mv ~/*.sql ~/ctbs
sudo -u postgres psql -f ~/ctbs/schema.sql
sudo -u postgres psql -f ~/ctbs/trigger.sql
sudo -u postgres psql -f ~/ctbs/data_base.sql
sudo -u postgres psql -f ~/ctbs/data_many.sql

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
# Change this to your workspace.
WorkingDirectory=/home/ubuntu/ctbs

# Path to executable, points straight to the jar file:
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