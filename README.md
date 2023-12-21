## School Management System

## Install project

```
git clone https://github.com/scandiy/school-management-system-be.git
cd school-management-system-be/
```

## Prerequisites

To run this project you need to install Docker from this link: [Docker](https://www.docker.com/)

## Run

```
docker-compose up -d
```
This command will run PostgreSQL, pgadmin and web app in Docker containers 

This application allows to perform CRUD operations on following school related entities: classes, students, subjects, marks.

Authentication implemented with JWT-based mechanism `/api/auth/signup`, `/api/auth/signin`
