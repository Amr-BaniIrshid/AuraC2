
#  Online Judge System – Graduation Project (Phase 1)

This repository contains the **backend core** of an Online Programming Contest System  
(similar to Codeforces / HackerRank).  
This phase covers **Authentication, System Design, Contest Structure, and Initial Implementation**.

---

##  Project Overview

| Feature | Status |
|--------|--------|
| User Registration + Login | ✔ Implemented |
| Email Verification | ✔ Implemented |
| JWT + Refresh Token Auth | ✔ Implemented |
| Contest → Problem → TestCase Design | ✔ Implemented |
| RabbitMQ Message Queue | ✔ Setup Complete |
| Judge0 Integration |  Next Phase |

This system is **not just CRUD** — it was designed to become a **real scalable online judge**,  
with asynchronous code execution and future-ready architecture.

---

##  Technologies Used

| Layer | Stack |
|------|------|
| Backend | Spring Boot 3 + Java 17 |
| Auth | JWT + Refresh Token + Cookie |
| Queue | RabbitMQ + JSON Message Converter |
| Database | MySQL + JPA/Hibernate |
| Docs | Postman + README.md |
| Future Integration | Judge0 API |

---

##  Folder Structure

```
src/main/java/com/securityProject/jwtAuthServer
│── config/          → Security & RabbitMQ configuration
│── controller/      → REST API (AuthController)
│── dto/             → Request & Response DTOs
│── entity/          → JPA Entities (User, Token, Contest, Problem…)
│── enums/           → Role, Difficulty, TokenType…
│── exception/       → GlobalExceptionHandler
│── repository/      → CRUD access to DB (JPA)
│── JwtAuthServerApplication.java
```

---

##  API Examples (Postman Testing)

###  Register User

```http
POST /api/auth/register
{
  "username": "amr",
  "email": "test@gmail.com",
  "password": "123456"
}
```

###  Login

```http
POST /api/auth/login
{
  "email": "test@gmail.com",
  "password": "123456"
}
```

###  Refresh Token (Auto via cookie)

```http
POST /api/auth/refresh
```

---

##  Ngrok Setup (To Test Gmail + JWT + Judge0)

Expose backend to the internet:

```bash
ngrok http 8080
```

Set your online base URL:

```
BASE_URL = https://your-ngrok-id.ngrok-free.app
```

Now Gmail verification & API calls work normally — even while running **localhost**.

---

## 🛠 Environment Variables

Create `.env` or add in `application.properties`:

```
DB_USERNAME=root
DB_PASSWORD=123456
JWT_SECRET=mysecretkey
SMTP_EMAIL=your.email@gmail.com
SMTP_PASS=your_gmail_app_password
```

---

##  Why RabbitMQ?

Judge0 responses may take several seconds.  
Direct HTTP calls block the request ❌

✔ So we used **RabbitMQ + JSON Converter** → asynchronous, scalable, microservice-ready.

```java
@Bean
public MessageConverter converter() {
    return new Jackson2JsonMessageConverter();
}
```

➡ System now supports **non-blocking code execution** — like real online judge platforms.

---

##  Section 6 — Design Challenges & Solutions

| Design Challenge                          | Design Decision                        | Impact                                  |
| ----------------------------------------- | -------------------------------------- | --------------------------------------- |
| Judge0 API is asynchronous                | Used RabbitMQ instead of direct HTTP   | Non-blocking & scalable execution       |
| Mapping multiple TestCases to one Problem | Used `OneToMany` relation              | Supports real contest logic             |
| Identifying which test failed             | Plan entity `SubmissionTestCaseResult` | Allows detailed feedback per user       |
| API was leaking DB entity                 | Added DTO layer for data transfer      | Clean architecture + safer API          |
| Token storage design                      | Created separate Token Entities        | Supports JWT rotation + OAuth in future |

✔ **All decisions were made for scalability, not only for passing the assignment.**

---

##  How to Run

```bash
mvn spring-boot:run
```

RabbitMQ (Docker):

```bash
docker run -d --hostname rabbit --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Access queue interface:  
`http://localhost:15672` → guest / guest

---

##  Next Steps (Assignment 4)

* Implement `Submission` entity  
* Full Judge0 API integration  
* Track per-test-case results  
* Leaderboard & ranking system  
* Deploy backend to cloud (Render / Railway / Fly.io)

---



