# Architecture

## Overview

The project uses a frontend-backend separated architecture.

- Frontend: Next.js
- Backend: Spring Boot
- Deployment: Docker Compose and Nginx

The MVP keeps the system simple and focuses on a complete resume diagnosis workflow.

## High-Level Flow

```text
User
  |
  v
Next.js Frontend
  |
  v
Spring Boot Backend
  |
  +--> PDF Text Parser
  |
  +--> AI Diagnosis Service
  |
  v
Diagnosis Report
  |
  v
Next.js Frontend
```

## Frontend Responsibilities

The frontend is responsible for:

- resume upload page
- upload state and error display
- showing analysis progress
- rendering the final diagnosis report

The frontend should not contain AI prompt logic or PDF parsing logic.

## Backend Responsibilities

The backend is responsible for:

- receiving uploaded PDF files
- validating file type and size
- extracting text from PDF files
- calling the AI diagnosis service
- returning structured diagnosis results to the frontend

## Deployment Responsibilities

The deployment directory will contain infrastructure files such as:

- Docker Compose configuration
- Nginx reverse proxy configuration
- environment examples
- future deployment scripts

## Initial Runtime Design

In local development:

- Next.js runs as the frontend development server.
- Spring Boot runs as the backend API server.
- The frontend sends API requests to the backend.

In Docker Compose deployment:

- Nginx receives external traffic.
- Nginx routes frontend and API traffic to the correct service.
- Backend configuration is provided through environment variables.

## Architecture Boundaries

The project will not introduce another backend framework, frontend framework, or deployment model without explicit confirmation.
