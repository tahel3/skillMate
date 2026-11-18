# SkillMate – Peer-to-Peer Skill Exchange Platform

SkillMate connects people who want to learn a skill with mentors who teach it: programming, music, cooking, painting, fitness and more. Learners search for skills, book sessions in a mentor's available slots, chat with mentors in real time and leave reviews. An AI advisor helps them decide what to learn next.

<img width="1917" height="908" alt="SkillMate screenshot 1" src="https://github.com/user-attachments/assets/7f0de553-7130-4744-acdc-cf2049aabac9" />
<img width="506" height="512" alt="SkillMate screenshot 2" src="https://github.com/user-attachments/assets/94d9fdc9-3426-4647-8494-71149735143f" />
<img width="1912" height="863" alt="SkillMate screenshot 3" src="https://github.com/user-attachments/assets/09eb5d84-9326-4250-9b84-bcd9a01b87ca" />

## 🚀 Key Features

- **Three user roles:** Learner, Mentor, or both. Authentication uses JWT with Spring Security and role-based access to endpoints.
- **Skill search and mentor profiles:** browse skills, view a mentor's skills, reviews and available dates.
- **Session booking:** mentors publish availability, and learners enroll in sessions.
- **Smart waitlist:** when a session is full, learners join a waitlist. A scheduled job (`@Scheduled`, every minute from 08:00 to 22:00) expires stale offers and notifies the next person in line by email.
- **Real-time chat:** learner–mentor messaging over WebSockets (STOMP + SockJS).
- **AI Skill Advisor:** a chat assistant that suggests skills and mentors. It calls an LLM through the OpenRouter API, with an automatic fallback model when the primary one is rate-limited.
- **Reviews and ratings** for mentors.
- **Email notifications** via Spring Mail (SMTP).

## 🛠 Tech Stack

| Layer | Technologies |
| --- | --- |
| Frontend | React 19, Vite, Redux Toolkit + RTK Query, React Router, Material UI (MUI), React Hook Form |
| Backend | Java 21, Spring Boot, Spring Security + JWT, Spring Data JPA (Hibernate), ModelMapper, Lombok |
| Real-time | Spring WebSocket, STOMP, SockJS |
| Database | H2 (file-based, zero setup) |
| Integrations | OpenRouter LLM API, SMTP email |
| Build | Maven (backend), npm (frontend) |

## 🏗 Architecture

```
React (Vite)  ──REST (JWT)──►  Spring Boot API  ──JPA──►  H2 database
     │                              │
     └──WebSocket (STOMP)──────────►│──► OpenRouter (AI advisor)
                                    └──► SMTP (email notifications)
```

The backend is organized in layers: **Controllers → Services → Repositories → Entities**, with DTOs mapped by ModelMapper.

## 📦 Getting Started

### Prerequisites
- Java 21+
- Node.js 20+ and npm

### 1. Clone
```bash
git clone https://github.com/tahel3/skillMate.git
cd skillMate
```

### 2. Backend
```bash
cd hobbiesManagment
cp src/main/resources/application.properties.example src/main/resources/application.properties
# edit application.properties: set jwt.secret, mail credentials and openrouter.api.key
./mvnw spring-boot:run
```
The API runs on `http://localhost:8080`. The H2 database file is created automatically in `./data`.

### 3. Frontend
```bash
cd frontendHobbiesManagment/SkillMate
npm install
npm run dev
```
The app runs on `http://localhost:5173`.

### Environment variables for deployment
| Where | Variable | Purpose |
| --- | --- | --- |
| Frontend | `VITE_API_URL` | Backend URL (default: `http://localhost:8080`) |
| Backend | `app.cors.allowed-origins` | Deployed frontend domain(s), comma separated |

## 👩‍💻 Author

**Tahel Eliya** – Full-Stack Developer · [GitHub](https://github.com/tahel3)
