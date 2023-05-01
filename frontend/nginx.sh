podman rm -f nginx
#podman run --name nginx --net=host -v "$(pwd)"/nginx.conf:/etc/nginx/nginx.conf:rw -d docker.io/nginx:stable-alpine
#podman run --name nginx --net=host --security-opt label=disable -d docker.io/nginx:stable-alpine
#podman cp nginx:/etc/nginx/conf.d/default.conf .

podman run --name nginx --net=host -v "$(pwd)"/default.conf:/etc/nginx/conf.d/default.conf:rw -d docker.io/nginx:stable-alpine
