# 📚 Library Management System

A RESTful **Library Management System** built using Java and Spring Boot, following a layered architecture to maintain clean separation of concerns, scalability, and maintainability.

The project implements book management operations using Spring Web, Spring Data JPA, DTOs, mapping, validation, and centralized exception handling. It is also designed to support future user registration, authentication, and role-based authorization.

---

## 📌 Table of Contents

- [Project Overview](#-project-overview)
- [Objectives](#-objectives)
- [Technology Stack](#-technology-stack)
- [System Architecture](#-system-architecture)
- [Architecture Explanation](#-architecture-explanation)
- [Features](#-features)
- [REST API Documentation](#-rest-api-documentation)
- [Sample Request](#-sample-request)
- [Validation and Exception Handling](#-validation-and-exception-handling)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Database Configuration](#-database-configuration)
- [Testing the API](#-testing-the-api)
- [Development Roadmap](#-development-roadmap)
- [Best Practices](#-best-practices)
- [Future Enhancements](#-future-enhancements)

---

## 🎯 Project Overview

The Library Management System is a backend application designed to manage book records through REST APIs.

The application follows a layered architecture in which controllers handle HTTP requests, services implement business logic, repositories interact with the database, and entities represent persistent data.

DTOs define API request and response structures, mapper classes convert between DTOs and entities, and centralized exception handling provides consistent error responses.

The architecture also provides a foundation for adding user management, authentication, and role-based access control in future development.

## 🎯 Objectives

- Develop RESTful APIs for book management.
- Implement Create, Read, Update, and Delete (CRUD) operations.
- Follow a layered architecture using Spring Boot.
- Separate API models from database entities using DTOs.
- Implement request validation and exception handling.
- Maintain readable, reusable, and maintainable code.
- Prepare the application for future authentication and authorization features.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming language |
| Spring Boot | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database persistence |
| Hibernate | JPA implementation and ORM |
| Jakarta Persistence | Entity mapping |
| Jakarta Bean Validation | Input validation |
| ModelMapper / custom mappers | DTO and entity conversion |
| SLF4J | Application logging |
| Maven | Dependency and build management |
| Relational database | Persistent storage |

*Use the actual database, dependencies, and mapper implementation configured in your project.*

---

## 🏗️ System Architecture

The following diagram illustrates the application architecture, including the API layer, service layer, data model, book management, and the planned user-management flow.

![Library Management System Architecture]("C:\Users\bunny\Downloads\diagram.png")
### Architecture Layers

1. **API Layer:** Receives HTTP requests and returns HTTP responses.
2. **Service Layer:** Implements business logic and coordinates application operations.
3. **Repository Layer:** Performs database operations through Spring Data JPA.
4. **Data Model Layer:** Contains entities, DTOs, and mapper classes.
5. **Exception Handling:** Handles application errors and produces consistent responses.

---

## 🔍 Architecture Explanation

### 1. API Client

The API client can be Postman, a frontend application, or another application that communicates with the backend.

It sends HTTP requests to endpoints exposed by the application.

Examples:

- `POST` — Create a book.
- `GET` — Retrieve books.
- `PUT` — Update a book.
- `DELETE` — Delete a book.

### 2. Book Controller

The controller receives incoming HTTP requests and delegates processing to the service layer.

Responsibilities:

- Define REST endpoints.
- Bind incoming request data.
- Trigger request validation.
- Call the appropriate service methods.
- Return responses with suitable HTTP status codes.

The controller should not contain database queries or complex business logic.

### 3. Book Service

The service layer contains the application's business logic.

For example, when updating a book, the service should verify that the book exists before applying changes.

Responsibilities:

- Process book-management operations.
- Apply business rules.
- Coordinate repository and mapper calls.
- Handle not-found conditions.
- Maintain consistency between book fields.

The `BookService` interface defines the operations, while `BookServiceImpl` provides their implementation, if structured that way in the project.

### 4. Book Repository

The repository layer communicates with the database using Spring Data JPA.

Typical operations include:

- Saving a book.
- Retrieving all books.
- Finding a book by ID.
- Updating a persisted record.
- Deleting a book.

Spring Data JPA provides common persistence operations through repository interfaces, reducing the need to write repetitive database-access code.

### 5. Book Entity

The `Book` entity represents a book record in the database.

Typical fields include:

- `id`
- `title`
- `author`
- `isbn`
- `category`
- `totalCopies`
- `availableCopies`

The entity is mapped to a database table using Jakarta Persistence annotations.

### 6. Data Transfer Objects (DTOs)

DTOs define the data exchanged between the client and backend.

- `BookRequestDTO` represents incoming book data.
- `BookResponseDTO` represents book data returned by the API.

DTOs help prevent the API from exposing internal persistence details and allow request validation to be defined independently of entity structure.

### 7. Book Mapper

The mapper converts between the `Book` entity and its DTO representations.

For example:

- Request DTO → Book entity.
- Book entity → Response DTO.

Keeping conversion logic in a mapper avoids repeating field-copying code across controllers and services.

### 8. Exception Handling

The global exception handler centralizes application error handling using Spring's `@RestControllerAdvice`.

For example, if a requested book does not exist, the service can throw `ResourceNotFoundException`. The exception handler can translate it into an HTTP `404 Not Found` response.

This approach makes error handling more consistent and keeps controllers clean.

### 9. User Management — Planned Extension

The architecture also includes components intended for future user management:

- `User` entity.
- User request and response DTOs.
- `UserMapper`.
- `UserRepository`.
- `UserService`.

These components can later support registration, password hashing, authentication, and role-based authorization.

**Security note:** Passwords should never be stored in plain text. Use a password-hashing mechanism such as BCrypt and enforce authorization rules on the backend.

---

## ✨ Features

### Implemented or currently developed

- Book entity and repository.
- Book CRUD operations.
- Service interface and implementation.
- Request and response DTOs.
- DTO-to-entity mapping.
- Request validation.
- Centralized exception handling.
- Structured application logging.

### Planned features

- User registration.
- Authentication using Spring Security.
- Role-based authorization.
- Borrowing and returning books.
- Pagination, sorting, and filtering.
- Automated testing.
- API documentation.

The status of each feature should reflect the code currently available in the repository.

---

## 🔗 REST API Documentation

The following table illustrates a typical REST API design. Confirm the actual endpoint mappings in `BookController` before using these routes.

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/books` | Add a new book |
| GET | `/api/books` | Retrieve all books |
| GET | `/api/books/{id}` | Retrieve a book by ID |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |

### Expected HTTP Status Codes

| Status | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 Created` | Resource created successfully |
| `204 No Content` | Request succeeded without a response body |
| `400 Bad Request` | Invalid request data |
| `404 Not Found` | Requested resource does not exist |
| `500 Internal Server Error` | Unexpected server-side error |

The exact status codes depend on how each controller endpoint is implemented.

---

## 📦 Sample Request

### Add a Book

**Endpoint:** `POST /api/books`

**Content-Type:** `application/json`

Example request body:

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "category": "Software Engineering",
  "totalCopies": 5,
  "availableCopies": 5
}
```

The JSON fields must match the actual `BookRequestDTO`.

If the application calculates `availableCopies` automatically, that field should instead be managed by the backend.

### Example Validation Rules

- Title must not be blank.
- Author must not be blank.
- Total copies must not be negative.
- Available copies must not be negative.
- Available copies must not exceed total copies.

The final rules should match the validations and business logic implemented in the project.

---

## 🛡️ Validation and Exception Handling

### Request Validation

Jakarta Bean Validation annotations can validate request data before the service processes it.

Common annotations include:

| Annotation | Purpose |
|---|---|
| `@NotBlank` | Rejects null, empty, or whitespace-only text |
| `@NotNull` | Rejects null values |
| `@Positive` | Requires a positive number |
| `@PositiveOrZero` | Allows zero or positive values |
| `@Size` | Restricts the length of text or collections |
| `@Valid` | Triggers validation of a request object |

For example, a request with a blank title or an invalid copy count should be rejected with HTTP `400 Bad Request` when the corresponding validation rules are configured.

### Global Exception Handling

A centralized exception handler can process:

- `ResourceNotFoundException`.
- `MethodArgumentNotValidException`.
- Other expected application exceptions.

Error responses should be consistent and useful. Internal stack traces, database credentials, and sensitive implementation details should not be exposed to clients.

---

## 📁 Project Structure

The following structure illustrates a recommended package organization. Adjust it to match your actual source files.

```text
library-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/library/management/
│   │   │       ├── config/
│   │   │       │   └── PasswordEncoderConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   └── BookController.java
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── BookRequestDTO.java
│   │   │       │   └── BookResponseDTO.java
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   ├── Book.java
│   │   │       │   └── User.java
│   │   │       │
│   │   │       ├── enums/
│   │   │       │   └── Role.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── ResourceNotFoundException.java
│   │   │       │
│   │   │       ├── mapper/
│   │   │       │   ├── BookMapper.java
│   │   │       │   └── UserMapper.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── BookRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── BookService.java
│   │   │       │   ├── BookServiceImpl.java
│   │   │       │   └── UserService.java
│   │   │       │
│   │   │       └── ManagementApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── docs/
│   └── images/
│       └── diagram.png
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

Some classes in this structure may be planned rather than implemented. Keep the repository structure and README synchronized as development progresses.

---

## 🚀 Getting Started

### Prerequisites

Install the following tools:

- Java Development Kit compatible with the version in `pom.xml`.
- Maven, or use the included Maven Wrapper.
- A supported relational database configured for the application.
- Git.
- Postman or another REST API client.

### Step 1: Clone the Repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd <YOUR_REPOSITORY_DIRECTORY>
```

Replace the placeholders with your actual Git repository URL and directory name.

### Step 2: Configure the Database

Create a database for the application and configure the connection properties.

See the [Database Configuration](#-database-configuration) section.

### Step 3: Run the Application

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

Alternatively, if Maven is installed:

```bash
mvn spring-boot:run
```

### Step 4: Test the API

Once the application starts successfully, send requests to the configured endpoints using Postman or curl.

Spring Boot commonly uses port `8080` by default, unless the application configuration specifies a different port.

---

## 🗄️ Database Configuration

Database settings should be configured through environment variables or a local configuration file excluded from Git.

Example `application.properties` settings:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Configure the correct database driver and JPA properties for your environment.

**Never commit database passwords, API keys, tokens, or other secrets to a public repository.**

---

## 🧪 Testing the API

Recommended manual test cases:

1. Create a book using valid input.
2. Submit a book with a blank title.
3. Submit a book with an invalid copy count.
4. Retrieve all books.
5. Retrieve an existing book by ID.
6. Request a book ID that does not exist.
7. Update an existing book.
8. Delete an existing book.
9. Verify that deleted records are no longer returned.
10. Confirm that invalid requests return meaningful error responses.

As the project grows, add automated unit tests for service logic and integration tests for controller and persistence behavior.

---

## 🗺️ Development Roadmap

- [x] Create the Book entity.
- [x] Implement the Book repository.
- [x] Implement book CRUD operations.
- [x] Create request and response DTOs.
- [x] Implement DTO/entity mapping.
- [x] Add request validation.
- [x] Add centralized exception handling.
- [ ] Complete user registration.
- [ ] Integrate secure password hashing.
- [ ] Implement authentication with Spring Security.
- [ ] Implement role-based authorization.
- [ ] Add unit and integration tests.
- [ ] Add OpenAPI/Swagger documentation.
- [ ] Add pagination, sorting, and filtering.
- [ ] Implement book borrowing and returning.
- [ ] Add transaction-safe inventory management.
- [ ] Prepare deployment documentation.

Update this checklist whenever a feature is completed or its implementation status changes.

---

## 💡 Best Practices

This project follows or aims to follow these backend development practices:

- **Separation of concerns:** Keep controllers, services, repositories, and mappers focused on their responsibilities.
- **Dependency injection:** Prefer constructor injection for required dependencies.
- **DTO-based APIs:** Avoid exposing persistence entities unnecessarily.
- **Input validation:** Validate incoming request data at the API boundary.
- **Centralized error handling:** Keep error responses consistent.
- **Meaningful logging:** Log useful diagnostic information without recording sensitive data.
- **Secure configuration:** Keep secrets outside version control.
- **Maintainable code:** Use meaningful names, small methods, and clear interfaces.
- **Automated testing:** Test normal behavior, invalid input, and failure scenarios.

---

## 🔮 Future Enhancements

Potential extensions include:

- User registration and login.
- Spring Security with JWT-based authentication.
- Role-based access control for administrators and library members.
- Book search by title, author, ISBN, or category.
- Pagination and sorting.
- Borrowing and returning workflows.
- Due-date tracking and overdue notifications.
- Fine calculation and borrowing history.
- API documentation with Swagger/OpenAPI.
- Docker-based deployment and CI/CD.
- Monitoring, structured logging, and production-ready error responses.

These are proposed enhancements, not claims that they are already implemented.

---

## 🤝 Contributing

Suggestions and improvements are welcome, especially those related to API design, validation, testing, security, and maintainability.

Before submitting changes, ensure the application builds successfully and the relevant tests pass.

---

## 📄 License

No license has been specified yet. Add a license file if you intend to distribute or allow others to reuse this project.

---

**Built with Java and Spring Boot as a hands-on backend engineering project.**
