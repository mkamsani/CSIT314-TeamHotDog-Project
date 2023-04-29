# CSIT314-TeamHotDog-Project

## Tasks:

1. [x] Meet up and discuss strengths.
2. [x] Figure out the project requirements and specifications.
3. [x] Determine the [coding languages](#Stack) to be used.
4. [x] Assign each person to their strengths and start breaking down tasks.
5. [x] Set up all relevant agile methodology tools required.
    * This repository + [Taiga](https://tree.taiga.io/project/isaacsimstudy-sim2023q2-hotdogbun/timeline)
6. [ ] Set up hosting for the project.
    * Examples:
        * [AWS Lambda](https://aws.amazon.com/lambda/)
        * [Azure Functions](https://azure.microsoft.com/services/functions/)
        * [Fly.io](https://fly.io/docs/languages-and-frameworks/dockerfile/)
7. [ ] Facilitate CI/CD:
    * e.g. circleCI

## Deliverables (23rd May 2023)

1. Final Deliverables (35 marks)
    * Report.docx
    * LiveDemo.mp4 (15 min)
    * Source code
2. Final presentation (5 marks)

## Stack

* [Java Spring Boot 3](./backend/cinema-ticket-booking-system/)
    * [main](./backend/cinema-ticket-booking-system/src/main/java/com/hotdog/ctbs/)
    * [test](./backend/cinema-ticket-booking-system/src/test/java/com/hotdog/ctbs/CinemaTicketBookingSystemApplicationTests.java)
    * [resources](./backend/cinema-ticket-booking-system/src/main/resources/)
* [Postgres 15](./backend/cinema-ticket-booking-system/src/main/resources/db)
* PHP?

## Frontend

### User Pages

```yaml
/index.html
├── login.html            # Shared for all users.
├── login-1.html          # If login fails, redirect to this page.
│
├── user/                 # Customer's pages.
│   └── TODO/
│       └── TODO.html
│
├── admin/                # TODO
│
├── owner/                # TODO
│
├── manager/              # TODO
│
│ # The pages below do not require login. Alternatively, all of the
│ # information for these pages can be displayed on the index page.
│
├── food-and-drinks.html
└── movies-available.html
```

### Assets

```yaml
/index.html
├── robots.txt
├── public/
│   ├── images/
│   │   ├── movies/
│   │   │   ├── movie1.jpg      # Replace with actual movie title.
│   │   │   ├── movie2.jpg
│   │   │   ├── movie3.jpg
│   │   │   └── placeholder.jpg # If no image is provided.
│   │   ├── others.svg
│   │   └── logo.svg            # Most likely a hot dog bun.
│   └── style.css               # For <header> and <footer>.
└── favicon.svg                 # Most likely a hot dog bun.
```

## Backend

### Setup

1. Sign up for [JetBrains Student License](https://www.jetbrains.com/shop/eform/students).
2. Install [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/download/#section=windows).
3. Install [WSL2](https://learn.microsoft.com/en-us/windows/wsl/install).
4. Install Ubuntu 20.04 LTS from the Microsoft Store.
5. Install [Docker Engine on Ubuntu](https://docs.docker.com/engine/install/ubuntu/).
6. Clone this repository.
7. Open the folder "cinema-ticket-booking-system" as an IntelliJ project.

## License

* [Spring Boot](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)
    * Dependency: [Data Faker](https://github.com/datafaker-net/datafaker/blob/main/LICENSE)
* [Postgres](https://www.postgresql.org/about/licence/)
* [PHP](https://www.php.net/license/index.php)