# AI Resume Review Backend

This directory contains the Spring Boot backend for AI Resume Review.

Current status: initial backend skeleton with a health check API and a resume analysis API. The resume API extracts text from uploaded PDF files and returns a mock diagnosis result through an AI diagnosis service abstraction. It does not include database integration or real AI service integration yet.

## Requirements

- Java 17
- Maven 3.x

## Local Development

Start the backend service:

```bash
mvn spring-boot:run
```

By default, the service starts on port `8080`.

## Health Check

After the service starts, test the health endpoint:

```bash
curl http://localhost:8080/api/health
```

Expected response:

```json
{"status":"ok"}
```

## Mock Resume Analysis

Analyze a PDF resume with a mock response:

```bash
curl -X POST http://localhost:8080/api/resume/analyze \
  -F "file=@/path/to/resume.pdf;type=application/pdf"
```

Current behavior:

- Accepts `multipart/form-data`.
- Requires the file field name to be `file`.
- Rejects empty files.
- Rejects non-PDF files.
- Checks basic PDF metadata and the `%PDF-` file header.
- Extracts PDF text with Apache PDFBox.
- Returns `extractedTextPreview` with the first 500 characters of extracted text.
- Uses `ResumeDiagnosisService` as the AI diagnosis abstraction.
- Uses `MockResumeDiagnosisService` as the current implementation.
- Does not call a real AI service or require an API key yet.
- Returns a mock diagnosis result containing `score`, `summary`, `extractedTextPreview`, `problems`, `suggestions`, and `jobFit`.
- Returns a clear error if PDF text parsing fails.

Example response:

```json
{
  "score": 72,
  "summary": "This is a mock resume diagnosis result.",
  "extractedTextPreview": "First 500 characters of extracted PDF text...",
  "problems": [],
  "suggestions": [],
  "jobFit": {
    "backendDevelopment": "...",
    "testDevelopment": "...",
    "devOpsSre": "..."
  }
}
```

## Tests

Run backend tests:

```bash
mvn test
```

## Docker

Build the backend image from the repository root:

```bash
docker build -t ai-resume-review-backend ./backend
```

Run the backend container:

```bash
docker run --rm -p 8080:8080 ai-resume-review-backend
```

Then test the health endpoint:

```bash
curl http://localhost:8080/api/health
```
