from pathlib import Path
import shutil, zipfile, os
import pypandoc

base = Path("/mnt/data/library_management_readme")
(base / "docs" / "images").mkdir(parents=True, exist_ok=True)

readme = r"""# Library Management System

A RESTful **Library Management System** built with **Java, Spring Boot, Spring Data JPA, and a relational database**. The project is being developed with a layered architecture to keep API handling, business logic, persistence, data transfer, and error handling separate and maintainable.

> **Project status:** Book CRUD operations, DTOs, mapping, validation, and centralized exception handling are part of the current implementation. User registration and role-based authorization are planned extensions.

## Table of Contents

- [Overview](#overview)
- [Goals](#goals)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Architecture Walkthrough](#architecture-walkthrough)
- [Main Modules](#main-modules)
- [Book API](#book-api)
- [Data Validation and Error Handling](#data-validation-and-error-handling)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Testing the API](#testing-the-api)
- [Development Roadmap](#development-roadmap)
- [Engineering Practices](#engineering-practices)

## Overview

The application exposes REST endpoints for managing books in a library. Requests enter through the controller layer, are processed by service-layer business logic, and are persisted through Spring Data repositories. DTOs define the API payloads, while mapper classes convert between DTOs and entities. Validation and centralized exception handling help return consistent, useful responses to clients.

The design also provides a foundation for adding users, roles, authentication, and authorization without mixing those concerns into the book-management logic.

## Goals

- Implement Create, Read, Update, and Delete (CRUD) operations for books.
- Separate controllers, services, repositories, entities, DTOs, and mappers.
- Validate incoming request data before it reaches business logic.
- Return meaningful HTTP status codes and handle errors centrally.
- Keep the codebase easy to extend with user management and role-based access control.
- Practice production-oriented Spring Boot backend design.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Spring Boot | Application framework and dependency management |
| Spring Web | REST controllers and HTTP request/response handling |
| Spring Data JPA | Repository abstraction and database persistence |
| Jakarta Persistence (JPA) | Entity mapping |
| Jakarta Bean Validation | Request and field validation |
| ModelMapper / mapper classes | Conversion between DTOs and entities |
| SLF4J | Application logging |
| Relational database | Persistent storage; configure the database used by your environment |
| Maven | Build and dependency management |

> Update this table if your final `pom.xml` or database configuration differs from the stack listed above.

## Architecture

The following diagram illustrates the current layered design and the planned user-management path.

![Library Management System architecture](docs/images/library-management-architecture.png)

### Architecture principles

1. **Controller layer:** Owns HTTP endpoints, request binding, and response status codes. It delegates business work instead of implementing it directly.
2. **Service layer:** Contains application rules and coordinates repository and mapper calls.
3. **Repository layer:** Uses Spring Data JPA to read and write persistent records.
4. **Entity layer:** Represents persisted domain data, such as `Book` and `User`.
5. **DTO layer:** Defines request and response payloads so the API does not need to expose persistence entities directly.
6. **Mapper layer:** Converts between DTOs and entities, keeping conversion code out of controllers and services.
7. **Exception-handling layer:** Centralizes known application errors and produces consistent HTTP responses.

## Architecture Walkthrough

### 1. API client to controller

An API client—such as Postman, a frontend application, or another service—sends an HTTP request to a REST endpoint. `BookController` receives the request, validates/binds its payload, calls the appropriate service method, and returns an HTTP response.

Controllers should remain thin: request handling belongs here, while business decisions belong in the service layer.

### 2. Controller to service contract

`BookController` depends on the `BookService` interface rather than directly implementing business logic. The service contract defines the operations the controller can request. A concrete implementation such as `BookServiceImpl` carries out those operations.

Using an interface helps keep the API layer independent from the implementation and makes unit testing easier.

### 3. Service to repository

The service uses `BookRepository` for persistence. The repository is responsible for database operations such as saving a book, finding all books, finding a book by ID, and deleting a book.

The service should check business conditions—such as whether a book exists before updating or deleting it—and raise a meaningful exception when an operation cannot be completed.

### 4. DTOs and mapping

`BookRequestDTO` represents data accepted from a client. `BookResponseDTO` represents data returned to the client. `BookMapper` converts between these DTOs and the `Book` entity.

This separation helps prevent clients from directly controlling internal entity fields and gives the API flexibility to evolve without exposing every database detail.

### 5. Entity and persistence

The `Book` entity models the persisted book record. Its fields currently include the book ID, title, author, ISBN, category, total copies, and available copies. JPA maps the entity to a database table.

When copy-management rules are expanded, the service should ensure that values such as `availableCopies` remain consistent with `totalCopies`.

### 6. Centralized exception handling

The global exception handler uses Spring's `@RestControllerAdvice` to translate application exceptions into structured HTTP responses. For example, a missing book can be represented by a `ResourceNotFoundException` and returned as HTTP `404 Not Found`.

Validation failures should return HTTP `400 Bad Request`, while unexpected server failures should not expose stack traces or internal implementation details to API clients.

### 7. Planned user-management path

The diagram also shows a user-management design with a `User` entity, role information, user DTOs, `UserMapper`, `UserRepository`, and a user service contract. This is a foundation for future registration and authentication work.

When implemented, passwords must be stored as secure hashes (for example, using BCrypt), never as plain text. Role-based authorization should be enforced on the server, not only in the frontend.

## Main Modules

### Book management

The book module is responsible for managing book records.

| Operation | Responsibility |
|---|---|
| Add book | Accept validated book data and persist a new record |
| List books | Return the available book records |
| Get book by ID | Retrieve one book or report that it does not exist |
| Update book | Verify that the target exists, then update allowed fields |
| Delete book | Verify that the target exists, then remove it |

### User management — planned

The user module is intended to support registration and future authentication. The planned components include:

- `User` entity with user details and role information.
- User request/response DTOs.
- `UserMapper` for conversion between DTOs and entities.
- `UserRepository` for persistence.
- `UserService` contract and implementation for user-related business logic.

Authentication and role-based authorization should be added only after the registration flow, password hashing, and security configuration are designed and tested.

## Book API

The exact route names should match the mappings in `BookController`. A typical REST design is shown below; adjust paths if your implementation uses different ones.

| Method | Example endpoint | Purpose |
|---|---|---|
| `POST` | `/api/books` | Create a book |
| `GET` | `/api/books` | List books |
| `GET` | `/api/books/{id}` | Retrieve a book by ID |
| `PUT` | `/api/books/{id}` | Update a book |
| `DELETE` | `/api/books/{id}` | Delete a book |

### Example request payload

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

Use the request DTO's actual validation rules and field names as the source of truth. If the API calculates `availableCopies` itself, omit that field from the client payload and enforce the rule in the service layer.

### Typical responses

- `200 OK` — a successful read or update, depending on the endpoint contract.
- `201 Created` — a resource was created successfully.
- `204 No Content` — a delete succeeded and the API returns no body.
- `400 Bad Request` — request payload or validation failed.
- `404 Not Found` — the requested book does not exist.
- `500 Internal Server Error` — an unexpected server-side failure occurred.

## Data Validation and Error Handling

Validation annotations on request DTO fields help reject invalid data before business logic executes. Examples include:

- `@NotBlank` for required text fields.
- `@NotNull` for required values that must not be null.
- `@Positive` or `@PositiveOrZero` for numeric constraints, depending on the field's rules.
- `@Size` for text length limits.
- `@Valid` on controller request parameters to trigger validation.

Examples of useful business rules:

- A title and author must not be blank.
- The ISBN should follow the format required by the project.
- `totalCopies` must not be negative.
- `availableCopies` must not be negative or exceed `totalCopies`.
- An update or delete should return a not-found response if the book ID does not exist.

`GlobalExceptionHandler` should return a predictable error shape, such as a timestamp, status, message, and—where appropriate—field-level validation errors. Avoid returning stack traces to clients.

## Project Structure

The package names below are a recommended representation. Keep the names aligned with the actual packages in your repository.

```text
src/main/java/com/library/management/
├── config/
│   └── PasswordEncoderConfig.java
├── controller/
│   └── BookController.java
├── dto/
│   ├── BookRequestDTO.java
│   └── BookResponseDTO.java
├── entity/
│   ├── Book.java
│   └── User.java                 # planned / as implemented
├── enums/
│   └── Role.java                 # if used by the current implementation
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── mapper/
│   ├── BookMapper.java
│   └── UserMapper.java           # planned / as implemented
├── repository/
│   ├── BookRepository.java
│   └── UserRepository.java       # planned / as implemented
├── service/
│   ├── BookService.java
│   ├── BookServiceImpl.java
│   └── UserService.java          # planned / as implemented
└── ManagementApplication.java
```

## Getting Started

### Prerequisites

- JDK compatible with the Java version declared in `pom.xml`.
- Maven, or use the included Maven Wrapper.
- A relational database supported by your configured Spring Boot driver.
- Git and an API client such as Postman or curl.

### 1. Clone the repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd <YOUR_REPOSITORY_DIRECTORY>
```

Replace the placeholders with your repository URL and directory name.

### 2. Configure the database

Create a database for the application, then configure the connection using environment variables or your local, uncommitted configuration file. See [Configuration](#configuration).

### 3. Run the application

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

If the Maven Wrapper is not included, use `mvn spring-boot:run` after installing Maven.

### 4. Verify the application

Once startup completes, send a request to one of the configured endpoints—for example, the book-list endpoint. The actual host and port are determined by your Spring Boot configuration; the default port is commonly `8080`.

## Configuration

Keep credentials out of Git. Use environment variables or a local configuration file that is excluded by `.gitignore`.

Typical settings include:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Configure the database driver, JPA options, and schema-management strategy to match the database and environment. Do not use production credentials in example files or README documentation.

## Testing the API

Recommended checks for the book endpoints:

1. Create a valid book and verify that the API returns a success status.
2. Submit invalid data, such as a blank title or a negative copy count, and verify a `400 Bad Request` response.
3. Retrieve all books and verify the created record appears.
4. Retrieve a book by an existing ID.
5. Retrieve a non-existent ID and verify a `404 Not Found` response.
6. Update an existing book and confirm the persisted values change.
7. Delete a book and confirm it is no longer returned.
8. Test malformed request bodies and verify that errors do not expose stack traces.

As the project grows, add service unit tests and controller/repository integration tests.

## Development Roadmap

- [x] Book entity and repository
- [x] Book CRUD service and controller
- [x] Request/response DTOs
- [x] DTO/entity mapping
- [x] Request validation
- [x] Centralized exception handling
- [ ] Complete user registration flow
- [ ] Enforce password hashing and secure credential handling
- [ ] Add Spring Security authentication
- [ ] Add role-based authorization
- [ ] Add automated unit and integration tests
- [ ] Add API documentation (for example, OpenAPI/Swagger)
- [ ] Add pagination, sorting, and filtering for book listings
- [ ] Add borrowing/return workflows and transaction-safe copy tracking
- [ ] Add deployment instructions and production observability

Update the checklist as features are completed.

## Engineering Practices

- Keep controllers focused on HTTP concerns.
- Keep business rules in services, not controllers.
- Use DTOs at the API boundary and avoid exposing persistence entities unnecessarily.
- Prefer constructor injection for dependencies.
- Use meaningful exception types and consistent response formats.
- Log useful diagnostic context without logging passwords, tokens, or other secrets.
- Keep configuration and credentials environment-specific.
- Add tests for normal cases, invalid input, and not-found scenarios.
- Document any endpoint or schema changes in this README.

## Contributing

This is a learning-oriented backend project. Contributions and suggestions that improve maintainability, validation, testing, security, or API design are welcome. Before submitting changes, ensure the project builds and the relevant tests pass.

## License

Add a license here if you intend to distribute or reuse this project publicly.
"""

# Use pypandoc as required for Markdown generation, then preserve the authored Markdown exactly if conversion fails.
md_path = base / "README.md"
try:
    converted = pypandoc.convert_text(readme, "md", format="md", extra_args=["--standalone"])
    md_path.write_text(converted, encoding="utf-8")
except Exception:
    md_path.write_text(readme, encoding="utf-8")

shutil.copy2("/mnt/data/diagram.png", base / "docs" / "images" / "library-management-architecture.png")

zip_path = Path("/mnt/data/library-management-readme-package.zip")
with zipfile.ZipFile(zip_path, "w", zipfile.ZIP_DEFLATED) as zf:
    for path in base.rglob("*"):
        if path.is_file():
            zf.write(path, path.relative_to(base))

print("Created:")
print(md_path)
print(base / "docs" / "images" / "library-management-architecture.png")
print(zip_path)
