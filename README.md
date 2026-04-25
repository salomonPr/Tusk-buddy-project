# Task-Buddy

A full-stack task management application built with **Vue 3** (frontend) and **Spring Boot** (backend), using **MongoDB** as the database and **JWT** for authentication.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Backend](#backend)
- [Frontend](#frontend)
- [How Integration Works](#how-integration-works)
- [API Reference](#api-reference)
- [Authentication Flow](#authentication-flow)
- [Error Handling](#error-handling)

---

## Project Structure

```
tusk-buddy projet/
├── TuskBuddy_backend/          ← Spring Boot REST API
│   ├── src/main/java/com/api/tuskbuddy_backend/
│   │   ├── config/             ← Security, JWT filter, CORS
│   │   ├── controller/         ← API endpoints
│   │   ├── dto/                ← Request and response shapes
│   │   ├── entity/             ← MongoDB document models
│   │   ├── exception/          ← Custom exceptions + global handler
│   │   ├── repository/         ← MongoDB queries
│   │   └── service/            ← Business logic
│   ├── src/main/resources/
│   │   └── application.yaml    ← App configuration
│   └── pom.xml                 ← Maven dependencies
│
└── task-buddy/                 ← Vue 3 SPA
    ├── src/
    │   ├── assets/             ← Global CSS
    │   ├── components/
    │   │   ├── auth/           ← LoginPage.vue, SignupPage.vue
    │   │   ├── common/         ← NavBar.vue, FeatureCard.vue
    │   │   ├── LandingPage.vue
    │   │   ├── TaskList.vue    ← Main dashboard
    │   │   └── NotFoundPage.vue
    │   ├── router/             ← Vue Router routes
    │   ├── services/
    │   │   └── api.js          ← All HTTP calls to the backend
    │   ├── App.vue
    │   └── main.js
    ├── vite.config.js          ← Vite + proxy config
    └── package.json
```

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 4.0.5 | Web framework |
| Spring Security | included | Authentication & authorization |
| Spring Data MongoDB | included | Database access |
| MongoDB | latest | Database |
| JJWT | 0.12.6 | JWT token generation and validation |
| Lombok | latest | Reduces boilerplate code |
| SpringDoc OpenAPI | 3.0.2 | Swagger API documentation |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| Vue 3 | 3.5.27 | UI framework |
| Vue Router | 4.6.4 | Client-side routing |
| Axios | 1.15.0 | HTTP client |
| Vite | 7.3.1 | Build tool and dev server |
| Tailwind CSS | 3.4.19 | Utility CSS |

---

## Prerequisites

Make sure you have these installed before running the project:

- **Java 21** — [Download](https://adoptium.net/)
- **Node.js 20+** — [Download](https://nodejs.org/)
- **MongoDB** — [Download](https://www.mongodb.com/try/download/community)
- **Maven** — included via `mvnw` wrapper, no install needed

---

## Getting Started

### 1. Start MongoDB

```bash
sudo systemctl start mongod
```

Verify it is running:

```bash
sudo systemctl status mongod
```

### 2. Start the Backend

```bash
cd TuskBuddy_backend
./mvnw spring-boot:run
```

The backend starts on **http://localhost:9100**

### 3. Start the Frontend

```bash
cd task-buddy
npm install
npm run dev
```

The frontend starts on **http://localhost:5173**

### 4. Open the App

Go to **http://localhost:5173** in your browser.

---

## Backend

### Configuration — `application.yaml`

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/task-buddy   # MongoDB connection

server:
  port: 9100                                      # Backend runs on this port

jwt:
  secret: your-secret-key-here                   # Used to sign JWT tokens
  expiration-ms: 86400000                        # Token valid for 24 hours
```

### Package Structure

```
config/
  SecurityConfig.java           ← CORS, JWT filter, security rules
  JwtService.java               ← Generates and validates JWT tokens
  JwtAuthFilter.java            ← Reads token from every request
  CustomAuthenticationEntryPoint.java  ← Returns JSON on 401

controller/
  UserController.java           ← POST /register, POST /login, GET /me
  TaskController.java           ← GET, POST, PUT, DELETE /api/tasks

dto/
  LoginRequest.java             ← { email, password }
  UserRegistrationRequest.java  ← { username, email, password }
  AuthResponse.java             ← { token, id, username, email }
  UserResponse.java             ← { id, username, email }
  ErrorResponse.java            ← { timestamp, status, error, message, path }

entity/
  User.java                     ← MongoDB users collection
  Task.java                     ← MongoDB tasks collection
  Priority.java                 ← Enum: HIGH, MEDIUM, LOW
  Status.java                   ← Enum: TODO, IN_PROGRESS, DONE

exception/
  GlobalExceptionHandler.java   ← Handles all exceptions in one place
  TaskNotFoundException.java    ← Thrown when task ID not found
  UserNotFoundException.java    ← Thrown when user email not found
  UnauthorizedException.java    ← Thrown on bad login credentials
  ForbiddenException.java       ← Thrown when user doesn't own the resource

repository/
  UserRepository.java           ← findByEmail, findByUsername
  TaskRepository.java           ← findByOwnerId, search by title

service/
  UserService.java              ← Register, find user
  TaskService.java              ← CRUD operations for tasks
  CustomUserDetailsService.java ← Loads user by email for Spring Security
```

### Swagger UI

Once the backend is running, visit:

```
http://localhost:9100/swagger-ui.html
```

---

## Frontend

### Key Files

```
services/api.js       ← Single file for all HTTP calls + token interceptor
router/index.js       ← All page routes including 404 catch-all
components/
  auth/LoginPage.vue  ← Calls POST /api/users/login, stores token
  auth/SignupPage.vue ← Calls POST /api/users/register
  TaskList.vue        ← Dashboard: loads, creates, updates, deletes tasks
  NotFoundPage.vue    ← Shown on any unknown URL
```

### Available Routes

| URL | Page | Protected |
|---|---|---|
| `/` | Landing page | No |
| `/login` | Login | No |
| `/signup` | Register | No |
| `/tasks` | Dashboard | Yes (redirects to /login) |
| `/*` | 404 Not Found | No |

### npm Scripts

```bash
npm run dev       # Start development server on port 5173
npm run build     # Build for production (outputs to dist/)
npm run preview   # Preview the production build locally
```

---

## How Integration Works

The frontend and backend are two separate applications that communicate over HTTP.

### The Vite Proxy

During development, Vue runs on port `5173` and Spring Boot on port `9100`. The Vite proxy bridges them:

```js
// vite.config.js
proxy: {
  '/api': {
    target: 'http://localhost:9100',
    changeOrigin: true
  }
}
```

Any request Vue makes to `/api/...` is automatically forwarded to `http://localhost:9100/api/...`. The browser never sees port 9100.

### The Axios Interceptor

Every HTTP request automatically gets the JWT token attached:

```js
// services/api.js
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers['Authorization'] = `Bearer ${token}`
  return config
})
```

This means you never manually add the token in any component — it happens automatically on every call.

### Request Lifecycle

```
Vue component calls getTasks()
        ↓
api.js interceptor adds:  Authorization: Bearer eyJ...
        ↓
Vite proxy forwards to:   http://localhost:9100/api/tasks
        ↓
JwtAuthFilter validates the token
        ↓
TaskController.getAllTasks() runs
        ↓
Returns only tasks owned by the authenticated user
        ↓
Vue renders the task cards
```

---

## API Reference

### Auth Endpoints (public — no token required)

#### Register
```
POST /api/users/register
Content-Type: application/json

{
  "username": "salomon",
  "email": "salomon@example.com",
  "password": "secret123"
}

Response 201:
{
  "id": "64abc123",
  "username": "salomon",
  "email": "salomon@example.com"
}
```

#### Login
```
POST /api/users/login
Content-Type: application/json

{
  "email": "salomon@example.com",
  "password": "secret123"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": "64abc123",
  "username": "salomon",
  "email": "salomon@example.com"
}
```

### User Endpoints (protected — token required)

#### Get current user
```
GET /api/users/me
Authorization: Bearer <token>

Response 200:
{
  "id": "64abc123",
  "username": "salomon",
  "email": "salomon@example.com"
}
```

### Task Endpoints (protected — token required)

#### Get all tasks
```
GET /api/tasks
Authorization: Bearer <token>

Response 200:
[
  {
    "id": "64def456",
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "priority": "MEDIUM",
    "status": "TODO",
    "ownerId": "64abc123"
  }
]
```

#### Create a task
```
POST /api/tasks
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "priority": "MEDIUM",
  "status": "TODO"
}

Response 201: the created task object
```

#### Update a task
```
PUT /api/tasks/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Buy groceries",
  "priority": "HIGH",
  "status": "IN_PROGRESS"
}

Response 200: the updated task object
```

#### Delete a task
```
DELETE /api/tasks/{id}
Authorization: Bearer <token>

Response 204: No Content
```

#### Search tasks by title
```
GET /api/tasks/search?title=grocery
Authorization: Bearer <token>

Response 200: array of matching tasks
```

---

## Authentication Flow

```
1. User submits email + password on /login
          ↓
2. POST /api/users/login sent to Spring Boot
          ↓
3. Spring Security verifies credentials against MongoDB
          ↓
4. JwtService generates a signed token (valid 24h):
   eyJhbGciOiJIUzI1NiJ9   ← Header  (algorithm)
   .eyJzdWIiOiJqb2huIn0   ← Payload (email + expiry)
   .SflKxwRJSMeKKF2QT4f   ← Signature (tamper-proof)
          ↓
5. Frontend stores token in localStorage
          ↓
6. Every future request includes:
   Authorization: Bearer eyJhbGci...
          ↓
7. JwtAuthFilter on backend:
   - Reads the token from the header
   - Validates the signature using the secret key
   - Checks the token has not expired
   - Extracts the email from inside the token
   - Loads the user from MongoDB
   - Sets the user as authenticated
          ↓
8. Controller receives the request with the user already identified
```

---

## Error Handling

All errors return a consistent JSON shape:

```json
{
  "timestamp": "2026-04-24T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id: 64def456",
  "path": "/api/tasks/64def456"
}
```

| Status | When |
|---|---|
| 400 Bad Request | Blank fields, invalid email format, duplicate username/email |
| 401 Unauthorized | Wrong password, missing or expired token |
| 403 Forbidden | Trying to update or delete another user's task |
| 404 Not Found | Task or user does not exist, unknown URL |
| 500 Internal Server Error | Unexpected server error |
