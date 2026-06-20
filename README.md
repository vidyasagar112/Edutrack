# 🎓 EduTrack — Intelligent Learning Management System

An end-to-end Learning Management System combining a **Spring Boot 4** REST API, an **AI-powered Python Flask** analytics engine, and a fully responsive **Angular 19** frontend. Built as a final year B.Tech project with role-based access for Students, Instructors, and Admins.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![Angular](https://img.shields.io/badge/Angular-19-red)
![Python](https://img.shields.io/badge/Python-Flask-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Roles & Permissions](#-roles--permissions)
- [Screenshots](#-screenshots)
- [Team](#-team)
- [License](#-license)

---

## 📖 Overview

EduTrack solves a simple problem — traditional LMS platforms give students content but no insight into **how they're actually performing**. EduTrack closes that gap with a dedicated machine learning microservice that predicts scores, flags weak subjects, and estimates dropout risk in real time.

The system is built as **three independent services** that communicate over REST:

```
Angular 19  ⇄  Spring Boot 4 API  ⇄  MySQL 8
                      ⇅
              Python Flask ML Service
```

---

## 🏗 Architecture

| Layer | Technology | Port | Responsibility |
|---|---|---|---|
| **Frontend** | Angular 19, Bootstrap 5 | `4200` | UI, role-based routing, JWT session handling |
| **Backend API** | Spring Boot 4.0.3, Spring Security 7 | `8080` | Auth, business logic, REST APIs, persistence |
| **ML Service** | Python 3, Flask, Scikit-learn | `5000` | Score prediction, weak-area detection, dropout risk |
| **Database** | MySQL 8.0 | `3306` | Relational data store (10 tables) |

---

## ✨ Features

### 🎓 Student
- Browse, search, and filter courses by subject/category
- Enroll in courses and track section-by-section progress
- Attempt timed MCQ quizzes with instant auto-grading
- Detailed quiz review — see correct vs. wrong answers
- AI-powered analytics: predicted scores, weak subjects, dropout risk
- Rate and review courses
- Editable personal profile (PRN, DOB, category, etc.)

### 👨‍🏫 Instructor
- Create, publish/unpublish, and manage courses
- Build course sections and upload materials (PDF, DOCX, XLSX, PPT)
- Create quizzes with MCQ questions and time limits
- View enrolled students and their individual profiles
- Track per-course performance analytics

### 🛡 Admin
- Centralized dashboard with live system stats
- Manage all users — view, enable/disable, delete (with cascading cleanup)
- Separate Student / Instructor / All-user views
- Send direct emails to any user
- Monitor all courses across the platform

### 🤖 AI / ML
- Linear Regression model for score trend prediction
- Rule-based weak-area detection per subject
- Dropout risk scoring from activity patterns
- Personalized study suggestions

### 🔐 Security & Auth
- JWT-based stateless authentication with BCrypt password hashing
- Role-based access control (`ROLE_STUDENT`, `ROLE_INSTRUCTOR`, `ROLE_ADMIN`)
- Email verification on registration
- Forgot/reset password via email
- Remember-me sessions and inactivity timeout warnings

---

## 🛠 Tech Stack

<table>
<tr>
<td valign="top" width="25%">

**Backend**
- Java 17
- Spring Boot 4.0.3
- Spring Security 7
- Spring Data JPA / Hibernate
- JWT (jjwt)
- Swagger / OpenAPI

</td>
<td valign="top" width="25%">

**Frontend**
- Angular 19 (standalone)
- TypeScript
- Bootstrap 5
- RxJS
- Chart.js

</td>
<td valign="top" width="25%">

**ML Service**
- Python 3.11
- Flask + Flask-CORS
- Scikit-learn
- NumPy / Pandas

</td>
<td valign="top" width="25%">

**Infra / Tools**
- MySQL 8.0
- Git & GitHub
- Mailtrap (dev email)
- Postman / Swagger UI

</td>
</tr>
</table>

---

## 📁 Project Structure

```
EduTrack/
├── edutrack-backend/       # Spring Boot REST API
│   ├── src/main/java/com/edutrack/
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Spring Data repositories
│   │   ├── service/        # Business logic
│   │   ├── controller/     # REST controllers
│   │   ├── security/       # JWT + Spring Security config
│   │   ├── dto/             # Request/response DTOs
│   │   └── config/         # CORS, Swagger config
│   └── src/main/resources/application.properties
│
├── edutrack-ml/             # Python Flask ML microservice
│   ├── app.py
│   ├── routes/analytics.py
│   ├── services/predictor.py
│   └── services/data_processor.py
│
├── edutrack-frontend/       # Angular 19 SPA
│   └── src/app/
│       ├── core/            # guards, interceptors, services
│       ├── shared/          # models, shared components
│       └── modules/         # auth, dashboard, courses, quiz, analytics...
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

```
Java 17+
Node.js 18+ & Angular CLI 19
Python 3.11+
MySQL 8.0
```

### 1. Clone the repository

```bash
git clone https://github.com/vidyasagar112/Edutrack.git
cd Edutrack
```

### 2. Setup MySQL

```sql
CREATE DATABASE edutrack_db;
```

### 3. Run the Backend

```bash
cd edutrack-backend
# update src/main/resources/application.properties with your MySQL + Mailtrap creds
mvnw spring-boot:run
```
Backend → `http://localhost:8080`
Swagger UI → `http://localhost:8080/swagger-ui/index.html`

### 4. Run the ML Service

```bash
cd edutrack-ml
python -m venv venv
venv\Scripts\activate        # Windows
pip install -r requirements.txt
python app.py
```
ML Service → `http://localhost:5000`

### 5. Run the Frontend

```bash
cd edutrack-frontend
npm install
ng serve
```
Frontend → `http://localhost:4200`

---

## 📚 API Documentation

Interactive API docs are available via Swagger once the backend is running:

```
http://localhost:8080/swagger-ui/index.html
```

Key endpoint groups: `/api/auth`, `/api/courses`, `/api/enrollments`, `/api/quizzes`, `/api/sections`, `/api/documents`, `/api/ratings`, `/api/analytics`, `/api/progress`, `/api/profile`, `/api/admin`.

---

## 🔑 Roles & Permissions

| Feature | Student | Instructor | Admin |
|---|:---:|:---:|:---:|
| Browse / enroll in courses | ✅ | — | — |
| Attempt quizzes | ✅ | — | — |
| Create / publish courses | — | ✅ | ✅ |
| Upload course materials | — | ✅ | ✅ |
| View enrolled students | — | ✅ (own) | ✅ |
| Manage all users | — | — | ✅ |
| Send platform emails | — | — | ✅ |

---

## 🖼 Screenshots

> Add screenshots of the Landing Page, Dashboards, Quiz flow, and Analytics page here.

---

## 👥 Team

| Name | Role |
|---|---|
| **Vidyasagar Kamble** | Full-stack Development, Backend, ML Integration |
| **Shreyas Mane** | Frontend Development, Testing |

**Guide:** Prof. D. J. Waghmare
**Institution:** Shri Tuljabhavani College of Engineering, Tuljapur (DBATU)

---

## 📄 License

This project is developed for academic purposes as part of the B.Tech final year curriculum at DBATU.

---

<div align="center">

Made with ☕ and 🤖 by the EduTrack Team

</div>
