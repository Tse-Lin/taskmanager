# Task Manager REST API

A Java-based task management REST API built with Spring Boot, MySQL, and Spring Data JPA. The project demonstrates layered architecture, CRUD operations, automated testing, Docker containerization, and CI/CD.

## Tech Stack

- **Language:** Java 27
- **Framework:** Spring Boot 4.1.2
- **Database:** MySQL 8.4
- **Persistence:** Spring Data JPA / Hibernate
- **Testing:** JUnit 5, Mockito, MockMvc
- **API Documentation:** OpenAPI / Swagger UI
- **Build Tool:** Maven
- **Containerization:** Docker / Docker Compose
- **CI/CD:** GitHub Actions / GitHub Container Registry

## Architecture

The application follows a layered architecture:

```text
Client / Swagger UI
        |
    Controller
        |
      Service
        |
    Repository
        |
      MySQL
```

- **Controller:** Handles HTTP requests and responses.
- **Service:** Implements business logic.
- **Repository:** Provides database access through Spring Data JPA.
- **Entity:** Represents persistent task data.
- **DTO:** Validates incoming API requests.
- **Exception Handler:** Provides consistent error responses.

## Features

- Create, retrieve, update, and delete tasks
- Persist task data in MySQL
- Validate task titles
- Return appropriate HTTP status codes
- Handle invalid requests and missing tasks
- Generate interactive API documentation
- Run automated unit and controller tests
- Build and publish Docker images through GitHub Actions

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/tasks` | Retrieve all tasks |
| GET | `/tasks/{id}` | Retrieve a task by ID |
| POST | `/tasks` | Create a task |
| PUT | `/tasks/{id}` | Update a task |
| DELETE | `/tasks/{id}` | Delete a task |

### Example: Create Task

Request:

```http
POST /tasks
Content-Type: application/json
```

```json
{
  "title": "Learn Spring Boot"
}
```

Response (`201 Created`):

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "completed": false
}
```

## Running with Docker

### Prerequisites

- Docker Desktop
- Docker Compose

Create a `.env` file in the project root:

```dotenv
MYSQL_ROOT_PASSWORD=your_password
```

Start the application:

```bash
docker compose up --build -d
```

Access the API:

`http://localhost:8080/tasks`

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

Stop the containers:

```bash
docker compose down
```

## Running Locally

Requires Java 27, Maven Wrapper, and MySQL.

Configure the database environment variables and start the application:

```bash
./mvnw spring-boot:run
```

## Automated Testing

Run all tests:

```bash
./mvnw clean verify
```

The project includes unit tests for service logic and controller tests for API behavior, input validation, and HTTP status codes.

## CI/CD

GitHub Actions automates the following workflow:

1. Checkout source code
2. Set up Java
3. Start the MySQL test service
4. Compile and run automated tests
5. Package the application
6. Build a Docker image after CI succeeds
7. Publish the image to GitHub Container Registry

Docker images are tagged with `latest` and a Git commit SHA.

## Future Improvements

- Pagination and sorting
- User authentication and authorization
- Database migrations
- Integration tests using Testcontainers
- Production deployment configuration

## License

No license has been specified.