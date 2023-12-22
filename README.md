## School Management System

## Install project

```
git clone https://github.com/scandiy/school-management-system-be.git
cd school-management-system-be/
```

## Prerequisites

To run this project you need to install Docker from this link: [Docker](https://www.docker.com/)

Java 21 and Maven must be installed on your system to build the project.

## Run

```
mvn package

docker-compose up -d
```
These commands will build the project and will run PostgreSQL, pgadmin and web app in Docker containers.

This application allows to perform CRUD operations on following school related entities: classes, students, subjects, marks.

Authentication implemented with JWT-based mechanism `/api/auth/signup`, `/api/auth/signin`
