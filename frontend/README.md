# AI Resume Review Frontend

This directory contains the Next.js frontend for AI Resume Review.

Current status: initial frontend skeleton with backend API integration. The page uploads a PDF file to the local Spring Boot backend and renders the returned diagnosis result.

## Requirements

- Node.js 20 or later
- npm

## Local Development

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

By default, the frontend runs at:

```text
http://localhost:3000
```

The backend should also be running at:

```text
http://localhost:8080
```

## Build

Create a production build:

```bash
npm run build
```

Start the production server after building:

```bash
npm run start
```

## Type Check

Run TypeScript checking:

```bash
npm run typecheck
```

## Docker

Build the frontend image from the repository root:

```bash
docker build -t ai-resume-review-frontend ./frontend
```

Run the frontend container:

```bash
docker run --rm -p 3000:3000 ai-resume-review-frontend
```

Then open:

```text
http://localhost:3000
```

## Current Page

The initial homepage includes:

- Page title: AI Resume Review
- PDF upload area
- Start diagnosis button
- Loading state
- Error message area
- Diagnosis result display area

When the user clicks the diagnosis button, the frontend sends a `multipart/form-data` request to:

```text
POST /api/resume/analyze
```

The uploaded file field name is `file`.

In Docker Compose deployment, Nginx proxies `/api/` requests to the backend service. When running the frontend directly with `npm run dev`, `next.config.ts` can rewrite `/api/*` requests to the backend service.

The rewrite target can be overridden with:

```bash
BACKEND_API_URL=http://localhost:8080
```
