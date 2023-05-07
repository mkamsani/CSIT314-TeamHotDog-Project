DOCKER_BE_ARGS = --security-opt label=disable
backend-jar:
	@echo "Building backend jar"
	podman run --rm --name=maven --security-opt label=disable -v "$(shell pwd)/backend":/home/build cgr.dev/chainguard/maven:openjdk-17  -B package -Dmaven.test.skip --file pom.xml
	podman kill maven

testing-things:
	@pwd
	@echo test
	@echo test and $(pwd)
	@echo test and $(shell pwd)
	@echo test and "$(shell pwd)/backend"

better_backend:
	podman run --rm -d --name=maven --entrypoint=/usr/bin/top cgr.dev/chainguard/maven:openjdk-17
	podman cp ./backend/. maven:/home/build
	podman exec -it -w /home/build maven mvn -B package -Dmaven.test.skip --file pom.xml
	podman exec -it -w /home/build maven ls -la ./target
	podman exec -it -w /home/build maven ls -la
	podman cp maven:/home/build/target/backend-0.0.5-SNAPSHOT.jar .
	podman kill maven
