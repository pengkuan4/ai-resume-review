# AI Resume Review

AI Resume Review is an AI-powered resume diagnosis website for computer science students and new graduates.

The project is currently in an early planning and repository initialization stage. The current repository contains documentation and base directories only. Frontend and backend business code has not been implemented yet.

## Project Introduction

AI Resume Review is designed to help computer science new graduates review their resumes before applying for technical roles.

The MVP will support a complete basic workflow: upload a PDF resume, extract resume text, send the text to an AI diagnosis process, and display a structured review report.

This project is also intended to be close to a real web application so it can later be used for operations practice, including deployment, reverse proxy configuration, logging, CI/CD, and monitoring.

## Target Users

- Computer science students preparing for internships or full-time jobs.
- New graduates applying for software development, operations, DevOps, testing, data, or other technical entry-level roles.
- Learners who want practical feedback on resume structure, technical project descriptions, and role readiness.

## MVP Features

The following features are planned for the MVP and are not fully implemented yet:

- PDF resume upload.
- PDF text extraction.
- AI-based resume diagnosis.
- Structured report output.
- Basic error handling for upload, parsing, and AI diagnosis failures.

The MVP will not include login, registration, payment, complex job recommendation, or long-term report storage.

## Technology Stack Plan

- Frontend: Next.js
- Backend: Spring Boot
- AI integration: backend service layer with prompt templates
- PDF parsing: backend-side PDF text extraction
- Deployment: Docker Compose and Nginx
- Version control and collaboration: Git and GitHub

## Current Project Structure

```text
ai-resume-review/
|-- backend/                  # Planned Spring Boot backend
|-- deployment/               # Planned Docker Compose and Nginx config
|-- docs/                     # Project documents
|   |-- 01_project_vision.md
|   |-- 02_requirements.md
|   |-- 03_architecture.md
|   `-- 04_development_plan.md
|-- frontend/                 # Planned Next.js frontend
|-- prompts/                  # Planned AI prompt templates
|-- research/                 # Research notes and references
|-- samples/                  # Sample resumes and test materials
|-- LICENSE
`-- README.md
```

## Development Stage Plan

### Stage 1: Project Initialization

- Create the base repository structure.
- Write project vision, requirements, architecture, and development plan documents.
- Keep the project scope focused on the MVP.

Current status: in progress.

### Stage 2: Frontend Skeleton

- Initialize the Next.js project under `frontend/`.
- Build the initial upload page.
- Prepare placeholder UI for the diagnosis report.

Current status: not started.

### Stage 3: Backend Skeleton

- Initialize the Spring Boot project under `backend/`.
- Add a basic health check endpoint.
- Prepare file upload API structure.

Current status: not started.

### Stage 4: PDF Upload and Parsing

- Upload PDF files from the frontend to the backend.
- Validate file type and file size.
- Extract text from uploaded PDF resumes.

Current status: not started.

### Stage 5: AI Diagnosis and Report Output

- Create the first AI diagnosis prompt.
- Integrate the backend with an AI service.
- Return a structured diagnosis result.
- Display the result in the frontend.

Current status: not started.

## Future Deployment Plan

The deployment plan will be implemented after the frontend and backend skeletons are ready.

Planned deployment work includes:

- Add Dockerfile configuration for frontend and backend services.
- Add Docker Compose configuration for local and server deployment.
- Add Nginx reverse proxy configuration.
- Manage environment variables with `.env` files and examples.
- Add health checks for services.
- Prepare deployment documentation under `deployment/` or `docs/`.
- Later add CI/CD, logging, monitoring, and backup practice.

