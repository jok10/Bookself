# Bookself

Bookself is a Spring Boot REST API that allows users to browse books, maintain personal bookshelves, write reviews, and manage user accounts. The system supports JWT-based authentication and role-based authorization, allowing administrators to manage the book catalogue while regular users can maintain their own reading collections.

Developed as part of **HBV501G Software Project 1** at the **University of Iceland**.

---

## Team 7

- María Björk Gunnarsdóttir
- Jóhann Kjartansson
- Silja Ástudóttir

---

## Features

### Authentication & Authorization
- User registration
- User login
- JWT authentication
- Role-based authorization
- User and Admin roles

### User Management
- View own profile
- Update own profile information
- Delete own account
- Upload profile picture
- View all registered users (Admin only)

### Book Catalogue
- View all books
- Retrieve a specific book
- Search for books
- Filter books by genre
- Add books (Admin)
- Update books (Admin)
- Delete books (Admin)

### Bookshelves
- View personal bookshelf
- Add books to personal bookshelf
- Remove books from personal bookshelf

### Reviews
- Create reviews
- Update own reviews
- Delete own reviews
- View reviews for a book
- Paginated review retrieval

---

## Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication

### Database
- PostgreSQL (Render)

### Development Tools
- Maven
- Git
- GitHub
- Postman

### External Services
- ImgBB (profile image hosting)

---

## Project Structure

```text
src/main/java/is/hi/hbv501g/bookself

├── controller
├── dto
├── model
├── repository
├── security
├── service
└── utilities
```

---

## Design Pattern

This project implements the **Singleton** design pattern through the `UsageStatistics` class.

### Purpose

The singleton tracks the total number of registered users during application runtime.

### Implementation

The class uses a private constructor and exposes a single access point:

```java
public static UsageStatistics getInstance()
```

This ensures that only one instance of the statistics tracker exists during application runtime.

### Usage

The singleton is used by the user management functionality:

- Incrementing the total user count when a new user account is created.
- Decrementing the total user count when a user account is deleted.

This implementation demonstrates practical use of the Singleton design pattern as required by the course assignment.

---

## Security

The application uses JSON Web Tokens (JWT) for stateless authentication.

### Authentication Flow

1. User submits username and password.
2. Spring Security validates the credentials.
3. A JWT token containing the user's username, ID, and role is generated. 
4. The token is returned to the client. 
5. Authenticated requests include the token in the Authorization header. 
6. The application uses the JWT claims to identify the current user and enforce authorization rules.

Example:

```http
Authorization: Bearer <jwt-token>
```

### Example JWT Payload

```json
{
  "iss": "library-api",
  "sub": "username",
  "userId": 15,
  "roles": [
    "USER"
  ]
}
```

The custom `userId` claim is used for ownership validation and user-specific operations.

---

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|----------|----------|----------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT |
| POST | `/api/auth/logout` | Logout |
| POST | `/api/auth/register-admin` | Register a new administrator (**Admin only**) |

### Users

| Method | Endpoint | Description |
|----------|----------|----------|
| GET | `/api/users/profile` | Get the authenticated user's profile |
| PUT | `/api/users/profile` | Update the authenticated user's profile |
| DELETE | `/api/users/profile` | Delete the authenticated user's account |
| POST | `/api/users/profile-picture` | Upload a profile picture |
| GET | `/api/users/all` | Retrieve all registered users (**Admin only**) |

### Bookshelf

| Method | Endpoint | Description |
|----------|----------|----------|
| GET | `/api/users/bookshelf` | Retrieve the authenticated user's bookshelf |
| POST | `/api/users/bookshelf/add` | Add a book to the authenticated user's bookshelf |
| DELETE | `/api/users/bookshelf/remove` | Remove a book from the authenticated user's bookshelf |

### Books

| Method | Endpoint | Description |
|----------|----------|----------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get a book by ID |
| GET | `/api/books/search` | Search books by title or author |
| GET | `/api/books/genre` | Filter books by genre |
| POST | `/api/books` | Create a new book (**Admin only**) |
| PUT | `/api/books/{id}` | Update a book (**Admin only**) |
| DELETE | `/api/books/{id}` | Delete a book (**Admin only**) |

### Reviews

| Method | Endpoint | Description |
|----------|----------|----------|
| GET | `/api/reviews/book/{bookId}` | Retrieve all reviews for a book |
| GET | `/api/reviews/book/{bookId}/page` | Retrieve paginated reviews for a book |
| POST | `/api/reviews` | Create or update the authenticated user's review |
| DELETE | `/api/reviews/book/{bookId}` | Delete the authenticated user's review |

---

## Running the Project

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- IntelliJ IDEA (recommended)

### Environment Variables

The following environment variables must be configured before running the application loca:

```text
pgusername=<database_username>
pgpassword=<database_password>
JWT_SECRET=<jwt_secret>
IMGBB_API_KEY=<imgbb_api_key>
```

A JWT secret can be generated using:

```bash
openssl rand -base64 32
```

---

## Starting the Application

Run the main application class directly through IntelliJ IDEA.

The API will be available at:

```text
http://localhost:8080
```

If not running locally, the deployed API is available at `https://bookself-otqw.onrender.com`.

---

## Example Login Request

### Request

```http
POST /api/auth/login
```

```json
{
  "username": "ilovebooks",
  "password": "12345"
}
```

### Response

```json
{
  "token": "<jwt-token>",
  "expiresIn": 3600
}
```

