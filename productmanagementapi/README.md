# Product Management REST API

A backend REST API built using **Spring Boot** to manage products and categories, with JWT-based authentication, role-based access control, database integration, and automated testing.

## Features

- Product CRUD operations
- Category CRUD operations
- DTO-based request and response handling
- Request validation
- Global exception handling
- Pagination and sorting
- Product search and filtering
- JPA relationships and JPQL queries
- User registration and login
- JWT-based authentication
- Password encryption using BCrypt
- Role-Based Access Control (USER and ADMIN)
- API documentation using Swagger / OpenAPI
- Unit testing using JUnit 5 and Mockito
- Controller testing using MockMvc

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3.5.16 | Backend framework |
| Spring Web | REST APIs |
| Spring Data JPA | Database operations |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| MySQL 8 | Relational database |
| Maven | Dependency management |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| MockMvc | Controller testing |
| Swagger / OpenAPI | API documentation |

## Security

- JWT-based authentication.
- BCrypt password hashing.
- Role-based access control.
- USER and ADMIN roles.
- Protected endpoints based on authorization rules.

## Testing

The project includes service-layer unit tests using JUnit 5 and Mockito, along with controller-layer tests using MockMvc.

Test coverage includes successful operations, exception handling, validation failures, and authorization-related scenarios.

## Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL 8
- IntelliJ IDEA or another Java IDE

### Database Setup

Create a MySQL database:

```sql
CREATE DATABASE product_db;
```

### Environment Variables

Configure the following environment variables before running the application:

```text
DB_URL=jdbc:mysql://localhost:3306/product_db
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secure_jwt_secret
```

The application reads these values from environment variables. Do not commit actual credentials or JWT secrets.

### Run the Application

Clone the repository:

```bash
git clone https://github.com/pranavparate2005-jpg/-springboot-api-test-2026.git
```

Open the `productmanagementapi` project directory and run:

```bash
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Use Swagger UI to explore and test the available API endpoints.

## Project Structure

```text
productmanagementapi/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/productmanagementapi/
│   │   └── resources/
│   └── test/
│       └── java/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .gitignore
```

## Author

**Pranav Parate**

- LinkedIn: https://linkedin.com/in/pranavparate-java
- GitHub: https://github.com/pranavparate2005-jpg
