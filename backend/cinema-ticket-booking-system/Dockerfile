# Instructions for building the application without Dockerfile:
# 1) Run top so that the container doesn't exit.
# 2) Copy everything from the host to the container.
# 3) Run the build the directory is one level below.
# 4) Copy the jar from /home/build/cinema-ticket-booking-system/target/ to the host.
# docker run --rm -d --name=maven --entrypoint=/usr/bin/top cgr.dev/chainguard/maven:openjdk-17
# docker cp . maven:/home/build
# docker exec -it -w /home/build/cinema-ticket-booking-system maven mvn -B package -Dmaven.test.skip --file pom.xml
# docker cp maven:/home/build/cinema-ticket-booking-system/target/cinema-ticket-booking-system-0.0.5-SNAPSHOT.jar .

# Instructions for building and running the application with Dockerfile:
FROM cgr.dev/chainguard/maven:openjdk-17 as builder
# Same COPY command as above, but grant chown to user and group 65532:
COPY --chown=65532:65532 ./. /home/build/
WORKDIR /home/build/
RUN mvn -B package -Dmaven.test.skip --file pom.xml
FROM cgr.dev/chainguard/jre:openjdk17
COPY --from=builder /home/build/target/cinema-ticket-booking-system-0.0.5-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# docker build --security-opt label=disable -t backend .
# docker run --rm --net=host --name=backend -d backend