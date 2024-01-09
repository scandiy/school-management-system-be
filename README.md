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

## Database

Accessing Database Information:

`URL`: http://localhost:5050

`Username`: admin@email.com

`Password`: postgrespw

Servers:

`Server Name`: School

`Password`: postgrespw

## API

Accessing the API with Postman:

To explore the API functionality, you can utilize [Postman](https://www.postman.com/).

API Collection Link: 
```
https://api.postman.com/collections/11790173-7310495b-fea1-44cb-9d03-c5fd2380961d?access_key=PMAT-01HKQRPGA9Y66KRCQR5Z218N3J
```

Import this collection link into Postman to access and test the API endpoints.
