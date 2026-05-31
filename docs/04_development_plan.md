# Development Plan

## Phase 1: Project Initialization

- Create the repository directory structure.
- Add project planning documents.
- Define MVP scope and technical boundaries.
- Avoid writing frontend or backend business code in this phase.

## Phase 2: Frontend Skeleton

- Initialize the Next.js frontend under `frontend/`.
- Create the basic upload page layout.
- Add placeholder report display components.
- Configure frontend linting and formatting if needed.

## Phase 3: Backend Skeleton

- Initialize the Spring Boot backend under `backend/`.
- Add a basic health check endpoint.
- Prepare file upload API structure.
- Configure environment variables for future AI service integration.

## Phase 4: PDF Upload and Parsing

- Implement PDF upload from frontend to backend.
- Validate file type and size.
- Extract text from uploaded PDF files.
- Return parsed text or parsing status for development verification.

## Phase 5: AI Diagnosis

- Design the first diagnosis prompt under `prompts/`.
- Implement AI service integration in the backend.
- Return a structured diagnosis result.
- Add basic error handling for AI request failures.

## Phase 6: Report Display

- Render AI diagnosis results in the frontend.
- Improve report readability.
- Add loading and failure states.
- Prepare sample resumes under `samples/` for manual testing.

## Phase 7: Deployment Practice

- Add Dockerfiles for frontend and backend.
- Add Docker Compose configuration.
- Add Nginx reverse proxy configuration.
- Document local deployment and server deployment steps.

## Phase 8: Operations Improvement

- Add application logs.
- Add health checks.
- Add basic CI checks.
- Prepare future monitoring and backup notes.
