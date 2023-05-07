FROM cgr.dev/chainguard/maven:openjdk-17 as spring-builder
COPY --chown=65532:65532 ./backend/. /home/build/
WORKDIR /home/build/
RUN mvn -B package -Dmaven.test.skip --file pom.xml

FROM docker.io/trafex/php-nginx
# Remove default nginx website
RUN rm -rf /var/www/html/*
# Install frontend and backend
COPY --from=spring-builder /home/build/target/cinema-ticket-booking-system-0.0.5-SNAPSHOT.jar /var/www/html/spring-boot.jar
COPY ./frontend/. /var/www/html/

# Add Spring Boot to supervisord configuration
USER root
RUN apk add openjdk17-jdk && \
printf \\n%s\\n "[program:spring-boot]" >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "command=java -jar /var/www/html/spring-boot.jar" >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "autostart=true                                 " >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "autorestart=true                               " >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "stderr_logfile=/var/log/spring-boot.err.log    " >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "stdout_logfile=/var/log/spring-boot.out.log    " >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "user=nobody                                    " >> /etc/supervisor/conf.d/supervisord.conf && \
printf %s\\n "environment=SPRING_PROFILES_ACTIVE=production  " >> /etc/supervisor/conf.d/supervisord.conf

# Append to nginx rerouting spring-boot requests to port 8080, the append is done at line 36.
# We have to be careful because there is code below line 36 that we don't want to overwrite.
# We use sed to append the code to the file.
# We use fastcgi to reroute requests to port 8080.
# We can only use fastcgi stuff in all the lines below.
# We use the following command to append the code to the file:
RUN \
sed -i '36i location /spring-boot {' /etc/nginx/conf.d/default.conf                 &&\
sed -i '37i proxy_pass http://localhost:8000/;' /etc/nginx/conf.d/default.conf      &&\
sed -i '38i proxy_http_version 1.1;' /etc/nginx/conf.d/default.conf                 &&\
sed -i '39i proxy_set_header Upgrade $http_upgrade;' /etc/nginx/conf.d/default.conf &&\
sed -i "40i proxy_set_header Connection 'upgrade';" /etc/nginx/conf.d/default.conf  &&\
sed -i '41i proxy_set_header Host $host;' /etc/nginx/conf.d/default.conf            &&\
sed -i '42i proxy_cache_bypass $http_upgrade;' /etc/nginx/conf.d/default.conf       &&\
sed -i '43i }' /etc/nginx/conf.d/default.conf

RUN \
sed -i '36i location /spring-boot {' /etc/nginx/conf.d/default.conf                 &&\
sed -i '37i proxy_pass http://localhost:8000/;' /etc/nginx/conf.d/default.conf      &&\
sed -i '38i proxy_http_version 1.1;' /etc/nginx/conf.d/default.conf                 &&\
sed -i '39i proxy_set_header Upgrade $http_upgrade;' /etc/nginx/conf.d/default.conf &&\
sed -i "40i proxy_set_header Connection 'upgrade';" /etc/nginx/conf.d/default.conf  &&\
sed -i '41i proxy_set_header Host $host;' /etc/nginx/conf.d/default.conf            &&\
sed -i '42i proxy_cache_bypass $http_upgrade;' /etc/nginx/conf.d/default.conf       &&\
sed -i '43i }' /etc/nginx/conf.d/default.conf

# User nobody will own the /var/www/html directory
RUN chown -R nobody:nobody /var/www/html

USER nobody
# docker build --security-opt label=disable -t ctbs .
# docker run --rm --net=host --name=ctbs -d --security-opt label=disable ctbs
# docker build --security-opt label=disable -t ctbs . && docker run --rm --net=host --name=ctbs -d --security-opt label=disable ctbs

# Remove dangling images
# docker rmi $(docker images -f "dangling=true" -q)