# ⚙️ Connect Sphere — Server (Backend)

This is the **backend** service of the **Connect Sphere** project.  
It provides RESTful APIs for handling user interactions, posts, authentication, and media uploads.  
The backend is built using **Spring Boot (Java)** and follows a clean, modular, and scalable architecture.

---

## 🚀 Overview

The server is responsible for:
- 🔐 User authentication and authorization (Spring Security & JWT)
- 📝 Post creation, update, and deletion
- 💬 Handling reactions and user interactions
- 📸 Uploading and serving media (images/videos)
- ⚡ Managing scalable and efficient API responses
- 🌍 Enabling CORS for frontend communication

It communicates directly with the **frontend client** (React app) running on port **5173**, while the backend itself runs on port **8080** by default.

---

## 🧩 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security (JWT based)**
- **MongoDB** for document-based data storage
- **AWS S3 / Firebase Storage** for media uploads
- **Maven** for dependency management
- **Spring Data MongoDB** for repository layer
- **Validation API** for request validation

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/connect-sphere.git
cd connect-sphere/server
```

### 2. Configure Environment Variables

Before running the application, make sure to configure the required environment variables or application properties.

You can either:
- Create a `.env` file in the root of the server directory, **or**
- Use the `application.properties` / `application.yml` file.

#### 🔧 Required Configuration
Make sure to provide the following (with your own values):

```
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/connect_sphere
# OR for production / cloud-hosted MongoDB
# spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster-url>/connect_sphere

# Server Port
server.port=8080

# JWT / Security Configuration
app.jwt.secret=YOUR_SECRET_KEY
app.jwt.expiration=86400000

# Media Storage (choose one)
# Firebase
firebase.config.path=/path/to/your/firebase-config.json

# OR AWS S3
aws.s3.bucket-name=YOUR_BUCKET_NAME
aws.s3.access-key=YOUR_ACCESS_KEY
aws.s3.secret-key=YOUR_SECRET_KEY
aws.s3.region=YOUR_REGION
```

⚠️ **Note:**  
Do not commit sensitive information like credentials, keys, or tokens to version control. Use environment variables or a secure config management service.

---

### 3. Run the Application

To start the backend server:
```bash
./mvnw spring-boot:run
```

Or package it and run the jar file:
```bash
mvn clean package
java -jar target/connect-sphere-server.jar
```

The backend will be available at:
```
http://localhost:8080
```

---

## 🧠 Notes

- Ensure your **MongoDB server** is running locally (`mongodb://localhost:27017`) before starting the application.  
- The backend automatically creates collections and manages schemas using Spring Data MongoDB.  
- Cross-Origin Resource Sharing (CORS) is enabled for local frontend testing.  
- You can customize configuration settings in the `application.yml` file.

---

## 🧾 Troubleshooting

- If you face CORS issues, ensure CORS configuration matches the frontend origin (default: `http://localhost:5173`).  
- Check that your MongoDB instance or URI is valid and accessible.  
- Review the application logs for stack traces or missing configurations.  

---

## 🧩 Developer Tip

You can test backend APIs using tools like **Postman** or **cURL** before integrating with the frontend.

---

> *Connect Sphere Backend — powered by Spring Boot and MongoDB for scalable and flexible data handling.*
