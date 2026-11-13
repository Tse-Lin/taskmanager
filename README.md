# Task Manager REST API

A Java-based task management REST API built with Spring Boot, MySQL, and Spring Data JPA. The project demonstrates layered architecture, CRUD operations, automated testing, Docker containerization, and CI/CD.

## Tech Stack

- **Language:** Java 27
- **Framework:** Spring Boot 4.1.2
- **Database:** MySQL 8.4
- **Persistence:** Spring Data JPA / Hibernate
- **Testing:** JUnit 5, Mockito, MockMvc, Testcontainers
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
- Persist task data in MySQL using Spring Data JPA
- Filter tasks by completion status
- Search task titles with case-insensitive keywords
- Support pagination and sorting
- Validate task titles and pagination parameters
- Return appropriate HTTP status codes
- Handle invalid requests and missing tasks with consistent JSON error responses
- Generate interactive API documentation using Swagger UI
- Run automated unit tests using JUnit 5 and Mockito
- Run controller tests using MockMvc
- Run MySQL integration tests using Testcontainers
- Containerize the application using Docker and Docker Compose
- Automatically build, test, and publish Docker images through GitHub Actions



## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/tasks` | Retrieve tasks with filtering, pagination, and sorting |
| GET | `/tasks/{id}` | Retrieve a task by ID |
| POST | `/tasks` | Create a task |
| PUT | `/tasks/{id}` | Update a task |
| DELETE | `/tasks/{id}` | Delete a task |

### Query Parameters

| Parameter | Description | Default |
|---|---|---|
| `page` | Page number (starting from 0) | 0 |
| `size` | Number of tasks per page (1–100) | 10 |
| `sort` | Sorting field and direction | `id,asc` |
| `completed` | Filter by completion status | All |
| `keyword` | Case-insensitive title search | None |

### Example: Search Tasks

GET /tasks?completed=false&keyword=Java&page=0&size=5&sort=id,desc

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


The project includes 28 automated tests covering:

- Service unit tests using JUnit 5 and Mockito
- Controller tests using MockMvc
- MySQL repository integration tests using Testcontainers
- Input validation and HTTP error responses
- Task filtering, keyword search, and pagination

All tests are executed automatically through GitHub Actions CI.


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

- User authentication and authorization
- Database migrations using Flyway
- Production deployment configuration


## License

No license has been specified.