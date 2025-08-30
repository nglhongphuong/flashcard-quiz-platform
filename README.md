# Flashcard and Quiz Learning Platform

**Personal capstone project (Jul 2025 – Sep 2025, 10 weeks)**  
-- A web-based learning platform that integrates flashcards and quizzes to help students review, memorize, and test knowledge more effectively. The system combines three main approaches: learning with flashcards, practicing with quizzes, and taking tests.
---
## Overview
-- This project was built as an **individual capstone project**.  
Its purpose is to provide an **interactive and flexible study tool** that supports both personal learning and community engagement.
---
## Main Features

### User Functions
- **Account Management**
  - Register a new account
  - Log in and log out
  - Update personal information  

- **Learning and Practice**
  - Flashcards: flip cards, mark progress
  - Quiz: generate practice questions based on selected flashcards
  - Test: create timed tests with customizable settings
  - Lesson Creation: manual input, upload from Excel/Word, or automatic generation with Gemini API
  - Study Scheduling: set reminders via Gmail notifications  

- **Community Interaction**
  - Join and study lessons created by others
  - Comment and rate shared lessons
  - Search, filter, and manage lessons (CRUD operations for personal content)

### Admin Functions
- Manage user accounts (create, update, delete, approve requests)
- Moderate lessons and content shared by users
- Generate statistics and reports about user activity and lesson effectiveness

---

## Non-Functional Requirements
- **Usability:** Simple and intuitive user interface, clear system messages  
- **Compatibility:** Works on common browsers (Chrome, Firefox, Edge)  
- **Security:** Passwords are encrypted; secure session management  

---

## Current Work in Progress
- Real-time chat with Firebase to improve community interaction  
- API testing with Postman (JSONPlaceholder) → [View API Collection](https://app.getpostman.com/join-team?invite_code=1896988308825b5f472a0ea81f8760bd4358a6cd73deca2c23a7815adc5ef95b&target_code=dcde96fbfb2901b36d80a2123632e938)  
- Deployment on AWS  
---
## Test Report
Progress is being tracked in the following document:  
[View Test Report](https://docs.google.com/spreadsheets/d/1kXi8L5MAiMwSSL7exDL4D8HUgQAzsIwN/edit?usp=sharing&ouid=112268585182906922050&rtpof=true&sd=true)
---
## Technologies
- **Frontend:** ReactJS  
- **Backend:** Spring Boot, FastAPI (Gemini API integration)  
- **Database:** MySQL  
- **Testing:** Postman, Selenium  
- **Other:** Gmail API for notifications  
