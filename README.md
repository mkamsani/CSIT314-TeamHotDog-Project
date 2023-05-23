# CSIT314-TeamHotDog-Project

## Deliverables (23rd May 2023)

Development files are located in the [development-files](./development-files) folder. \
See its [README.txt](./development-files/README.txt) for more information.

## Stack and Tooling

* [Controller & Entity: Java Spring Boot 3](./backend)
    * [entities](./backend/src/main/java/com/hotdog/ctbs/entity)
    * [controllers](./backend/src/main/java/com/hotdog/ctbs/controller)
    * [main source folder](./backend/src/main/java/com/hotdog/ctbs/)
* [Database: Postgres 15](./database)
* [Boundary: PHP](./frontend/)
* [CI/CD: GitHub](./.github/workflows/deploy.yml)
* [CI/CD: AWS Deployment](./.deploy/aws_push.sh)
* [CI/CD: Test controller of one user story](./backend/src/test/java/com/hotdog/ctbs/UserStoryLoginTests.java)

## License

* [Spring Boot](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)
* [Postgres](https://www.postgresql.org/about/licence/)
* [PHP](https://www.php.net/license/index.php)
