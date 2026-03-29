# Ecoms API (Spring Boot)

Simple e-commerce backend with:
- JWT authentication
- User registration/login
- Password reset code generation via Gmail SMTP
- Password change using generated code

## Tech Stack
- Java 17
- Spring Boot 4.0.4
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven

## Project Structure
- `src/main/java/com/black/ecoms/controller` - REST controllers
- `src/main/java/com/black/ecoms/service` - business logic
- `src/main/java/com/black/ecoms/security` - JWT filter and security config
- `src/main/resources/application.yaml` - app configuration

## Prerequisites
- Java 17+
- Maven 3.9+
- MySQL running locally
- Gmail account with App Password (for SMTP)

## Database Setup
Default DB config in `application.yaml`:
- URL: `jdbc:mysql://localhost:3306/ecoms_db`
- Username: `root`
- Password: `P@$$w0rdv1`

Create database if needed:
```sql
CREATE DATABASE ecoms_db;
```

## Email (SMTP) Setup
This app uses Gmail SMTP (`smtp.gmail.com:587`).

Set environment variables before running:
```bash
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-16-char-app-password"
export MAIL_FROM="your-gmail@gmail.com"
```

Notes:
- Use a Gmail App Password (not your normal Gmail password).
- You can use either grouped or ungrouped app password text.

## Run the App
From project root:
```bash
mvn spring-boot:run
```

App runs on:
- `http://localhost:8080`

## Security Rules
Public endpoints:
- `POST /api/users/register`
- `POST /api/users/login`

Authenticated endpoints:
- everything else (requires `Authorization: Bearer <jwt>`)

## API Quick Start

### 1) Register
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "P@ssword123"
  }'
```

### 2) Login (get JWT)
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "P@ssword123"
  }'
```

### 3) Generate password reset code (sends email)
```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "email": "john@example.com"
  }'
```

### 4) Change password using code
```bash
curl -X POST http://localhost:8080/api/users/change-password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "email": "john@example.com",
    "newPassword": "NewP@ssword123",
    "requestCode": "123456"
  }'
```

## Run Tests
Run all tests:
```bash
mvn test
```

Run only email-related tests:
```bash
mvn test -Dtest=EmailServiceTest,UsersServiceTest
```

## Common Issues
- `403 Forbidden` on `/generate-code`:
  - Missing/invalid JWT token.
- `Code generated but failed to send email`:
  - Invalid Gmail app password or SMTP credentials not exported.
- `Port 8080 already in use`:
  - Stop the process using port 8080, then restart.

## Existing Docs
This repo also includes additional guides like:
- `EMAIL_SENDING_QUICK_START.md`
- `EMAIL_SENDING_TEST_GUIDE.md`
- `START_HERE_EMAIL_TESTING.md`
