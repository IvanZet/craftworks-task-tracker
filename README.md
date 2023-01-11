## Description
This is my implementation of a task received from Craftworks. The application simulates a simple Task Management System
and incoming tasks are stored in a DB. The following functionality is implemented:
- Scheduler creating a new task every 10 seconds
- Tasks have Date-time fields and text fields
- Conversion of time from client's to DB's timezone and vice versa
- DTO for the task entity
- REST endpoints (compliant with the level 2 of REST-Maturity-Model Definition by Martin Fowler):
  - Fetch all tasks (ordered)
  - Fetch a single task
  - Create a single task
  - Update a single task
  - Delete a single task
  - Delete all tasks

## Technologies used
- Spring Boot 2.7
- PostgreSQL 15.1
- Hibernate 5.6
- ModelMapper
- Docker and Docker Compose
- JUnit 5
- Maven

## How to run it
```shell
cd src/main/docker
docker compose up
```
