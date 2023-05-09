FROM cgr.dev/chainguard/maven:openjdk-17 as spring-builder
COPY --chown=65532:65532 ./backend/. /home/build/
WORKDIR /home/build/
RUN mvn -B package -Dmaven.test.skip --file pom.xml
RUN ls -la target/ # backend-0.0.5-SNAPSHOT.jar