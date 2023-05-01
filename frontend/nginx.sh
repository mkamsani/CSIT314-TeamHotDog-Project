podman rm -f nginx
#podman run --rm --name nginx --net=host --security-opt label=disable  -d docker.io/nginx:stable-alpine
podman run --rm --name nginx -e NGINX_PORT=8181 --security-opt label=disable -v "$(pwd)"/nginx.conf:/etc/nginx/nginx.conf:ro -dd docker.io/nginx:stable-alpine
#podman run --name nginx --net=host --security-opt label=disable -d docker.io/nginx:stable-alpine
#podman cp nginx:/etc/nginx/conf.d/default.conf .

#podman run --name nginx --net=host -v "$(pwd)"/default.conf:/etc/nginx/conf.d/default.conf:rw -d docker.io/nginx:stable-alpine
