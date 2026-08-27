# JobTrack

JobTrack is a REST API for managing job applications, built with Java and Spring Boot.

The project is being developed as a practical backend portfolio project, with a focus on clean architecture, authentication, persistence, testing, and API design.

## Features

Current functionality includes:

* User registration
* User authentication
* JWT-based authentication
* Protected API endpoints
* Job application creation and retrieval
* Job application ownership per authenticated user
* PostgreSQL persistence
* Request validation
* Centralized API error handling
* Password hashing
* Unit tests for service-layer logic
* JWT generation tests

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Security
* Spring Data JPA
* PostgreSQL
* Docker Compose
* Maven
* JUnit 5
* Mockito
* JWT
* Git

## Project Structure

The project follows a layered structure:

```text
src/main/java/com/jobtrack/jobtrack
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── service
└── JobtrackApplication.java
```

Main responsibilities:

* `config` — security, JWT, and password configuration
* `controller` — REST endpoints
* `dto` — request and response objects
* `exception` — application exceptions and centralized error handling
* `model` — persistence entities and domain-related types
* `repository` — database access
* `service` — application and business logic

## Configuration

The application uses environment variables for sensitive configuration.

Required variables:

```env
POSTGRES_DB=jobtrack
POSTGRES_USER=your_database_user
POSTGRES_PASSWORD=your_database_password
JWT_SECRET=your_jwt_secret
```

The `.env` file is ignored by Git and should not be committed.

The Spring configuration reads these values from environment variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/${POSTGRES_DB:jobtrack}
spring.datasource.username=${POSTGRES_USER:jobtrack}
spring.datasource.password=${POSTGRES_PASSWORD}

security.jwt.secret=${JWT_SECRET}
security.jwt.expiration-seconds=3600
```

## Running the Database

The project includes a `compose.yaml` file for running PostgreSQL with Docker Compose.

Make sure your environment variables are configured, then run:

```bash
docker compose up -d
```

To check whether the container is running:

```bash
docker compose ps
```

## Running the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

By default, the application runs on:

```text
http://localhost:8080
```

## Running Tests

On Windows:

```powershell
.\mvnw.cmd test
```

On Linux or macOS:

```bash
./mvnw test
```

The project currently includes tests for:

* User account service
* Job application service
* Authentication service
* JWT service
* Spring application context

## API Overview

### Authentication

The API provides endpoints for user registration and login.

After a successful login, the API returns a JWT access token.

Protected endpoints require:

```http
Authorization: Bearer <access-token>
```

### Job Applications

Authenticated users can access their own job applications through the job application endpoints.

The application ensures that job application data belongs to the authenticated user.

## Security

Passwords are not stored in plain text.

Authentication is handled with Spring Security and JWT tokens.

Sensitive values such as database passwords and JWT secrets are provided through environment variables rather than committed directly to the repository.

## Development Workflow

Development is organized using Git feature branches.

Typical workflow:

```text
main
  └── feature/...
  └── docs/...
```

Changes are implemented and tested in their own branches before being merged into `main`.

## Roadmap

Planned improvements include:

* Update job applications
* Delete job applications
* Additional filtering and search
* Improved validation
* More unit and integration tests
* API documentation
* Pagination and sorting
* Deployment
* Further improvements to security and error handling

## Project Status

JobTrack is currently under active development.

The goal is to progressively evolve it into a complete backend portfolio project that demonstrates practical Java and Spring Boot development, API design, authentication, testing, database integration, and Git workflow.
