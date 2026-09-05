# Bookself

Backend API skeleton for a bookshelf application, built for a course project at
the University of Iceland (HBV501G).

> This is an early project skeleton for the first hand-in — not a finished
> product. Structure and endpoints will grow in later hand-ins.

## Stack

- Java 21
- Spring Boot 4.1.1 (Web, Data JPA)
- H2 in-memory database
- Maven

## Project structure

```
src/main/java/is/hi/hbv501g/bookself/
├── BookselfApplication.java   # entry point
├── model/                     # JPA entities
├── repository/                # Spring Data repositories
├── service/                   # business logic
└── controller/                # REST controllers
```

## Running

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`, and the H2 console at
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:bookself`, user
`sa`, empty password).

## Endpoints (skeleton)

| Method | Path              | Description       |
|--------|-------------------|--------------------|
| GET    | /api/books        | List all books     |
| GET    | /api/books/{id}   | Get a book by id   |
| POST   | /api/books        | Create a book      |
| PUT    | /api/books/{id}   | Update a book      |
| DELETE | /api/books/{id}   | Delete a book      |

## Tests

```bash
./mvnw test
```
