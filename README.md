# Cinema Ticket Booking System

[Group] project for [CSIT314], built with the [BCE] model.

Submitted revision: [3bd6e69c681da1b4e998a3179bf67c9a56e8af06.zip]

## Overview

* [Controller & Entity: Java Spring Boot 3]
    * [entities]
    * [controllers]
    * [main source folder]
* [Database: Postgres 15]
* [Boundary: PHP]
* [CI/CD: GitHub]
* [CI/CD: AWS Initialization]
* [CI/CD: AWS Deployment]
* [TDD: Test the controller of one user story.]
* [Local Deployment: Database]
* [Local Deployment: PHP & Nginx]

## Screenshots

### Login

![screenshot_0.png]

### Home

![screenshot_1.png]

## License

* [Spring Boot]
* [Postgres]
* [PHP]
* [Bootstrap]
* [TrafeX/docker-php-nginx]

## Advice

* Write [diagrams in code], so that they can be version controlled.
* Export [coded diagrams to draw.io] to add them to your report.
* **Pick a solution to write your report:**
  1. Microsoft Word
     * [Share OneDrive files and folders] so that your team can collaborate.
     * [Share a single document] if you don't intend to split sections into files. 
     * If you don't like the browser version, you can [open shared documents natively.]
     * Use the [draw.io add-in][Embed-Microsoft] to embed diagrams into your report.
     * Track [changes with Git. (Option 1)]
     * Track [changes with Git. (Option 2)]
     * Track [changes natively.]
  2. Google Docs
      * Use the [draw.io add-on][Embed-Google] to embed diagrams into your report.
  3. Markdown
      * Write text / tables / diagrams [in Markdown and export to .docx or PDF.] 
* **Pick a project management tool:**
    1. [Link GitHub issues to Taiga.]
    2. [Link GitHub issues to Jira.]
    3. [Use GitHub issues with GitHub Projects.]
* YouTube - [Agile Project Management with Kanban]

[//]: # (Links)

[Group]: https://github.com/mkamsani/CSIT314-TeamHotDogBuns/graphs/contributors
[CSIT314]: https://courses.uow.edu.au/subjects/2023/CSIT314?year=2023
[BCE]: https://www.youtube.com/watch?t=15m14s&v=JWcoiXNoKxk
[3bd6e69c681da1b4e998a3179bf67c9a56e8af06.zip]: https://github.com/mkamsani/CSIT314-TeamHotDogBuns/archive/3bd6e69c681da1b4e998a3179bf67c9a56e8af06.zip

[//]: # (Overview)
[Controller & Entity: Java Spring Boot 3]: ./backend
[entities]: ./backend/src/main/java/com/hotdog/ctbs/entity
[controllers]: ./backend/src/main/java/com/hotdog/ctbs/controller
[main source folder]: ./backend/src/main/java/com/hotdog/ctbs/
[Database: Postgres 15]: ./database
[Boundary: PHP]: ./frontend/
[CI/CD: GitHub]: ./.github/workflows/deploy.yml
[CI/CD: AWS Initialization]: ./.deploy/aws_init.sh
[CI/CD: AWS Deployment]: ./.deploy/aws_push.sh
[TDD: Test the controller of one user story.]: ./backend/src/test/java/com/hotdog/ctbs/UserStoryLoginTests.java
[Local Deployment: Database]: ./development-files/postgres.sh
[Local Deployment: PHP & Nginx]: ./development-files/nginx_php.sh

[//]: # (Images)
[screenshot_0.png]: ./screenshot_0.png
[screenshot_1.png]: ./screenshot_1.png

[//]: # (Licenses)
[Spring Boot]: https://raw.githubusercontent.com/spring-projects/spring-boot/main/LICENSE.txt
[Postgres]: https://raw.githubusercontent.com/postgres/postgres/REL_15_STABLE/COPYRIGHT
[PHP]: https://www.php.net/license/3_01.txt
[Bootstrap]: https://raw.githubusercontent.com/twbs/bootstrap/v5.0.2/LICENSE
[TrafeX/docker-php-nginx]: https://raw.githubusercontent.com/TrafeX/docker-php-nginx/master/LICENSE

[//]: # (Advice)

[diagrams in code]: https://mermaid.js.org/
[coded diagrams to draw.io]: https://drawio-app.com/blog/create-mermaid-diagrams-in-draw-io/
[Share OneDrive files and folders]: https://support.microsoft.com/en-us/office/share-onedrive-files-and-folders-9fcc2f7d-de0c-4cec-93b0-a82024800c07
[Share a single document]: https://support.microsoft.com/en-us/office/share-a-document-d39f3cd8-0aa0-412f-9a35-1abba926d354
[open shared documents natively.]: https://support.microsoft.com/en-us/office/see-files-others-have-shared-with-you-e0476dc7-bf2f-4203-b9ad-c809578b03e7
[Embed-Microsoft]: https://desk.draw.io/support/solutions/articles/16000061408-how-to-add-a-diagram-in-microsoft-word-excel-or-any-other-application
[changes with Git. (Option 1)]: https://tech.marksblogg.com/git-track-changes-in-media-office-documents.html
[changes with Git. (Option 2)]: https://github.com/tomashubelbauer/modern-office-git-diff
[changes natively.]: https://support.microsoft.com/en-us/office/track-changes-in-word-197ba630-0f5f-4a8e-9a77-3712475e806a
[Embed-Google]: https://desk.draw.io/support/solutions/articles/16000042524-use-diagrams-in-google-docs
[in Markdown and export to .docx or PDF.]: https://www.freshbrewed.science/diagrams-as-code-mermaid/index.html
[Use GitHub issues with GitHub Projects.]: https://docs.github.com/en/issues/planning-and-tracking-with-projects/learning-about-projects/about-projects
[Link GitHub issues to Taiga.]: https://docs.taiga.io/integrations-github.html
[Link GitHub issues to Jira.]: https://support.atlassian.com/jira-cloud-administration/docs/integrate-with-github/
[Agile Project Management with Kanban]: https://youtube.com/watch?v=CD0y-aU1sXo