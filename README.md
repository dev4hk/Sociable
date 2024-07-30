# Simple SNS Microservices

This project is for users to interact each other by posting their stories and photos & videos

## Features

- This project is consists of 8 different services
  - User Service
  - Post Service
  - Comment Service
  - Chat Service
  - Storage Service
  - Notification Service
  - Eureka Server
  - Gateway
- Users can create/login using their credentials
- Users can post their stories also make comments on other users posts
- Users get notified if other users like/make comments on their posts
- Users can follow each other
- Users can message each other

## Tech Stack

![Static Badge](https://img.shields.io/badge/Spring_Boot-blue)
![Static Badge](https://img.shields.io/badge/Spring_Web-blue)
![Static Badge](https://img.shields.io/badge/Spring_Security-blue)
![Static Badge](https://img.shields.io/badge/Open_Feign-blue)
![Static Badge](https://img.shields.io/badge/Netflix_Euraka-blue)
![Static Badge](https://img.shields.io/badge/Lombok-blue)
![Static Badge](https://img.shields.io/badge/Gradle-blue)
![Static Badge](https://img.shields.io/badge/JWT-blue)
![Static Badge](https://img.shields.io/badge/SpringDataJPA-blue)
![Static Badge](https://img.shields.io/badge/MySQL-blue)
![Static Badge](https://img.shields.io/badge/JUnit-blue)
![Static Badge](https://img.shields.io/badge/Mockito-blue)
![Static Badge](https://img.shields.io/badge/Web_Socket-blue)
![Static Badge](https://img.shields.io/badge/SSE-blue)
![Static Badge](https://img.shields.io/badge/ReactJS-blue)
![Static Badge](https://img.shields.io/badge/Recoil-blue)
![Static Badge](https://img.shields.io/badge/React_Query-blue)
![Static Badge](https://img.shields.io/badge/Axios-blue)
![Static Badge](https://img.shields.io/badge/MUI-blue)
![Static Badge](https://img.shields.io/badge/TailwindCSS-blue)


## Microservices Diagram
  ![](images/SNS_Diagram.png)

## Demo
- User Signup
  ![](images/signup.gif)

- Post
  ![](images/post.gif)

- Profile
  ![](images/profile.gif)

- Notification
  ![](images/notification.gif)

- Chat
  ![](images/chat.gif)


## Lessons Learned

- Polling
    - Client sends scheduled request and server sends response
    - Unnecessary API calls can happen if there's no updated data
    - Cannot receive response as a real-time update
- Long-Polling
    - Client sends request and server sends response if there is updated data as a real-time update
    - Improved from Polling, However, if data is updated very often, there is not so much difference from Polling
- SSE (Server-Sent Event)
    - Client can subscribe a specific event
    - If event occurs in the server, the server sends a response to the client
    - This is uni-directional, server to client
- WebSocket
    - Enables bidirectional communication between the server and the client
