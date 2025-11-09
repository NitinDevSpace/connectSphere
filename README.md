

# 🌐 Connect Sphere

**Connect Sphere** is a full-stack social media platform built to enable seamless interaction and media sharing among users. It’s designed with scalability, modularity, and performance in mind — capable of efficiently handling up to 100K+ active users through optimized API design and distributed storage strategy.

---

## 🚀 Overview

Connect Sphere offers a modern social experience that includes:
- 📝 Creating, editing, and deleting posts  
- 💬 Real-time feed updates  
- 👍 Reactions (like, love, etc.) and engagement tracking  
- 📸 Image and video uploads  
- 🔔 Instant feedback and notification handling  
- 🧱 Scalable backend architecture with modular service design  
- ⚡ Optimized caching and API performance for high user loads  

The platform is divided into two main components:
- **Client** → Frontend application  
- **Server** → Backend API service  

---

## 🧩 Tech Stack

### 🖥️ Frontend (Client)
- **React.js** with hooks and modular components  
- **Axios** for HTTP communication  
- **Vite** for blazing-fast development (runs on port `5173`)  
- **Responsive UI** using modern CSS  

### ⚙️ Backend (Server)
- **Spring Boot (Java)** for scalable RESTful APIs  
- **Spring Security** for authentication & authorization  
- **MySQL** for relational data storage  
- **AWS S3 / Firebase Storage** for handling image and video uploads  
- **Maven** for dependency management  
- **CORS-enabled APIs** for client-server communication  
- Runs on port `8080`  

---

## 🧪 Getting Started (Local Setup)

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/connect-sphere.git
cd connect-sphere
```

### 2. Start the Backend Server
```bash
cd server
./mvnw spring-boot:run
```

### 3. Start the Frontend Client
```bash
cd client
npm install
npm run dev
```

### 4. Access the Application
Once both services are running:
```
Frontend: http://localhost:5173  
Backend:  http://localhost:8080
```

---

## 🧠 Highlights

- Fully integrated RESTful API architecture  
- Secure authentication and token-based session management  
- Real-time interactions with efficient state synchronization  
- Modular code design for scalability and maintainability  
- Media upload support (images, videos)  
- Supports large-scale concurrent users  

---

## 🤝 Contributing
Contributions are welcome! Feel free to fork, raise issues, or submit pull requests.

---

## 🧾 License
This project is released under the **MIT License**.

---

## 📬 Contact
For questions or collaboration inquiries, connect via **LinkedIn** or email.

> *Connect Sphere — where ideas connect, and communities grow.*