# Drama Ticketing System - Backend
GitHub Repository Platform  

A **Spring Boot–based backend** for a modern **drama ticket booking system in Sri Lanka**.  
This backend powers drama and show management, ticket booking, ratings, and organizer workflows with role based authentication.  

---

## 📱 Features
- **User Roles & Authentication**
  - Admin, Theater Manager, Organizer, and Customer roles with secure access.
- **Drama & Show Management**
  - Add and manage dramas, cast, ratings, and related shows.
- **Theater & Open Area Ticketing**
  - Theater mode: structured seating with categories and pricing.
  - Open area mode: general admission tickets with stock-based control.
- **Seat Booking System**
  - Real-time seat availability and category-based pricing.
- **Organizer Management**
  - Organizers can independently set up shows with flexible venue options.
- **Review & Ratings**
  - Customers can rate dramas and leave feedback.
- **Integration Ready**
  - Built to integrate seamlessly with the React frontend.

---

## 🛠 Technologies Used
- **Backend Framework:** Spring Boot (Java)  
- **Database:** MySQL  
- **Frontend Integration:** React (via REST APIs)  
- **Architecture:** Layered Architecture (Controller, Service, Repository)  
- **Security:** Role-based access  
- **Tools:** IntelliJ IDEA / VS Code, MySQL Workbench, Git  

---

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/chathurak15/Drama-Ticketing-System-Backend.git
   cd Drama-Ticketing-System-Backend

---
Configure the database

Create a MySQL database (e.g., drama_ticketing).

Update your application.properties file:

properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/drama_ticketing
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
Build & Run

bash
Copy code
./mvnw spring-boot:run
Access the application

API will run at: http://localhost:8080

---
API Highlights

---
Authentication & Users
POST /api/auth/login – User login

POST /api/auth/register – User registration

---
Dramas
GET /api/dramas/{id} – Get drama details

POST /api/dramas – Add new drama

---
Shows & Booking
GET /api/shows/{id} – Get show details

POST /api/booking – Book tickets

---
Ratings
POST /api/ratings – Submit rating

GET /api/ratings/{dramaId} – Get drama ratings

---
Contributors
Chathura Kavindu

---
License
This project is licensed under the MIT License.
You are free to use, modify, and distribute this software.
