FROM cgr.dev/chainguard/maven:openjdk-17 as spring-builder
COPY --chown=65532:65532 ./backend/cinema-ticket-booking-system/. /home/build/
WORKDIR /home/build/
RUN mvn -B package -Dmaven.test.skip --file pom.xml

ARG S6_OVERLAY_VERSION=3.1.4.1
FROM alpine:latest

# Install s6-overlay
#ADD https://github.com/just-containers/s6-overlay/releases/download/v${S6_OVERLAY_VERSION}/s6-overlay-noarch.tar.xz /tmp
#ADD https://github.com/just-containers/s6-overlay/releases/download/v${S6_OVERLAY_VERSION}/s6-overlay-x86_64.tar.xz /tmp
ADD https://github.com/just-containers/s6-overlay/releases/download/v3.1.4.1/s6-overlay-noarch.tar.xz /tmp
ADD https://github.com/just-containers/s6-overlay/releases/download/v3.1.4.1/s6-overlay-x86_64.tar.xz /tmp
RUN tar -C / -Jxpf /tmp/s6-overlay-noarch.tar.xz && tar -C / -Jxpf /tmp/s6-overlay-x86_64.tar.xz

# Install frontend and backend
COPY ./frontend/. /var/www/html/
COPY --from=spring-builder /home/build/target/cinema-ticket-booking-system-0.0.5-SNAPSHOT.jar /var/www/html/spring-boot.jar

# Install dependencies
RUN apk add --no-cache \
  openjdk17-jre \
  curl \
  nginx \
  php81 \
  php81-ctype \
  php81-curl \
  php81-dom \
  php81-fpm \
  php81-gd \
  php81-intl \
  php81-mbstring \
  php81-mysqli \
  php81-opcache \
  php81-openssl \
  php81-phar \
  php81-session \
  php81-xml \
  php81-xmlreader

# Configure nginx
RUN \
mkdir -p /run/nginx && \
rm /etc/nginx/http.d/default.conf && \
printf %s "" > /etc/nginx/nginx.conf && \
printf %s\\n "worker_processes auto;" >> /etc/nginx/nginx.conf && \
printf %s\\n "error_log stderr warn;" >> /etc/nginx/nginx.conf && \
printf %s\\n "pid /run/nginx.pid;" >> /etc/nginx/nginx.conf && \
printf %s\\n "events {" >> /etc/nginx/nginx.conf && \
printf %s\\n "  worker_connections 1024;" >> /etc/nginx/nginx.conf && \
printf %s\\n "}" >> /etc/nginx/nginx.conf && \
printf %s\\n "http {" >> /etc/nginx/nginx.conf && \
printf %s\\n "    include /etc/nginx/mime.types;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    default_type application/octet-stream;" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    # Define custom log format to include response times" >> /etc/nginx/nginx.conf && \
printf %s\\n "    log_format main_timed '$remote_addr - $remote_user [$time_local] \"$request\" '" >> /etc/nginx/nginx.conf && \
printf %s\\n "                      '$status $body_bytes_sent \"$http_referer\" '" >> /etc/nginx/nginx.conf && \
printf %s\\n "                      '\"$http_user_agent\" \"$http_x_forwarded_for\" '" >> /etc/nginx/nginx.conf && \
printf %s\\n "                      '$request_time $upstream_response_time $pipe $upstream_cache_status';" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    /dev/stdout main_timed;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    error_log /dev/stderr notice;" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    keepalive_timeout 65;" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    # Write temporary files to /tmp so they can be created as a non-privileged user" >> /etc/nginx/nginx.conf && \
printf %s\\n "    client_body_temp_path /tmp/client_temp;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    proxy_temp_path /tmp/proxy_temp_path;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    fastcgi_temp_path /tmp/fastcgi_temp;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    uwsgi_temp_path /tmp/uwsgi_temp;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    scgi_temp_path /tmp/scgi_temp;" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    # Hardening" >> /etc/nginx/nginx.conf && \
printf %s\\n "    proxy_hide_header X-Powered-By;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    fastcgi_hide_header X-Powered-By;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    server_tokens off;" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    # Enable gzip compression by default" >> /etc/nginx/nginx.conf && \
printf %s\\n "    gzip on;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    gzip_proxied any;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    gzip_types text/plain application/xml text/css text/js text/xml application/x-javascript text/javascript application/json application/xml+rss;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    gzip_vary on;" >> /etc/nginx/nginx.conf && \
printf %s\\n "    gzip_disable \"msie6\";" >> /etc/nginx/nginx.conf && \
printf \\n >> /etc/nginx/nginx.conf && \
printf %s\\n "    # Include server configs" >> /etc/nginx/nginx.conf && \
printf %s\\n "    include /etc/nginx/conf.d/*.conf;" >> /etc/nginx/nginx.conf && \
printf %s\\n "}" >> /etc/nginx/nginx.conf

RUN mkdir -p /etc/nginx/conf.d

# Spring Boot Starts on port 8080. Use Nginx to reverse proxy anything 8080 to port 8000
RUN \
printf %s\\n "# Spring Boot reverse proxy" > /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "server {" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "    listen [::]:8000;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "    listen 8000;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "    server_name _;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf \\n >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "    location / {" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "        proxy_pass http://localhost:8080;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "        proxy_set_header Host \$host;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "        proxy_set_header X-Real-IP \$remote_addr;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "        proxy_set_header X-Forwarded-Proto \$scheme;" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "    }" >> /etc/nginx/conf.d/spring-boot.conf && \
printf %s\\n "}" >> /etc/nginx/conf.d/spring-boot.conf

# Configure default server
RUN \
printf %s\\n "# Default server definition" > /etc/nginx/conf.d/conf.d && \
printf %s\\n "server {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    listen [::]:8000 default_server;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    listen 8000 default_server;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    server_name _;" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    sendfile off;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    tcp_nodelay on;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    absolute_redirect off;" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    root /var/www/html;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    index index.php index.html;" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    location / {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        # First attempt to serve request as file, then" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        # as directory, then fall back to index.php" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        try_files $uri $uri/ /index.php?q=$uri&$args;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    }" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    # Redirect server error pages to the static page /50x.html" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    error_page 500 502 503 504 /50x.html;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    location = /50x.html {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        root /var/lib/nginx/html;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    }" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    # Pass the PHP scripts to PHP-FPM listening on php-fpm.sock" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    location ~ \.php$ {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        try_files $uri =404;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_split_path_info ^(.+\.php)(/.+)\$;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_pass unix:/run/php-fpm.sock;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_param SCRIPT_NAME $fastcgi_script_name;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_index index.php;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        include fastcgi_params;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    }" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    # Set the cache-control headers on assets to cache for 5 days" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    location ~* \.(jpg|jpeg|gif|png|css|js|ico|xml)\$ {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        expires 5d;">> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    }" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    # Deny access to . files, for security" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    location ~ /\. {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        log_not_found off;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        deny all;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    }" >> /etc/nginx/conf.d/conf.d && \
printf \\n >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    # Allow fpm ping and status from localhost" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    location ~ ^/(fpm-status|fpm-ping)\$ {" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        access_log off;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        allow 127.0.0.1;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        deny all;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        include fastcgi_params;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "        fastcgi_pass unix:/run/php-fpm.sock;" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "    }" >> /etc/nginx/conf.d/conf.d && \
printf %s\\n "}" >> /etc/nginx/conf.d/conf.d

# Configure PHP-FPM
ADD https://raw.githubusercontent.com/TrafeX/docker-php-nginx/master/config/fpm-pool.conf /etc/php81/php-fpm.d/www.conf
ADD https://raw.githubusercontent.com/TrafeX/docker-php-nginx/master/config/php.ini /etc/php81/conf.d/custom.ini

# Add nginx, php-fpm, and spring-boot to s6-svscan
RUN mkdir -p /etc/services.d/nginx /etc/services.d/php-fpm /etc/services.d/spring-boot && \
printf %s\\n "#!/usr/bin/execlineb -P" >> /etc/services.d/spring-boot/run && \
printf %s\\n "s6-setuidgid nobody" >> /etc/services.d/spring-boot/run && \
printf %s\\n "cd /var/www/html" >> /etc/services.d/spring-boot/run && \
printf %s\\n "exec /usr/bin/java -jar /var/www/html/spring-boot.jar" >> /etc/services.d/spring-boot/run && \
chmod +x /etc/services.d/spring-boot/run && \
printf %s\\n "#!/usr/bin/execlineb -P" >> /etc/services.d/nginx/run && \
printf %s\\n "s6-setuidgid nobody" >> /etc/services.d/nginx/run && \
printf %s\\n "s6-envdir /etc/s6/nginx" >> /etc/services.d/nginx/run && \
printf %s\\n "exec /usr/sbin/nginx -c /etc/nginx/nginx.conf -g 'daemon off;'" >> /etc/services.d/nginx/run && \
chmod +x /etc/services.d/nginx/run && \
printf %s\\n "#!/usr/bin/execlineb -P" >> /etc/services.d/php-fpm/run && \
printf %s\\n "s6-setuidgid nobody" >> /etc/services.d/php-fpm/run && \
printf %s\\n "exec /usr/sbin/php-fpm81 --nodaemonize --fpm-config /etc/php81/php-fpm.conf" >> /etc/services.d/php-fpm/run && \
chmod +x /etc/services.d/php-fpm/run

RUN apk add execline && mkdir -p /etc/s6/nginx

# Make sure files/folders needed by the processes are accessable when they run under the nobody user
RUN #chown -R nobody.nobody /var/www/html /run /var/lib/nginx /var/log/nginx /etc
# Switch to use a non-root user from here on
USER nobody
# Add application
# Expose the port nginx is reachable on
EXPOSE 8080

# docker build --security-opt label=disable -t ctbs .
# docker run --rm -it --entrypoint=/init -u root --net=host ctbs