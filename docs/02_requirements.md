# Requirements

## MVP Features

### Resume Upload

- Users can upload a PDF resume from the frontend.
- The system should reject unsupported file types.
- The system should provide a clear error message when upload or parsing fails.

### PDF Text Parsing

- The backend extracts text from the uploaded PDF.
- The extracted text is used only for diagnosis in the MVP.
- The initial version does not need to preserve the original PDF layout.

### AI Diagnosis

- The backend sends parsed resume text to an AI model using a controlled prompt.
- The AI response should be structured enough for the frontend to display as a report.
- The diagnosis should focus on computer science new graduate resumes.

### Report Output

- The frontend displays the diagnosis result after analysis completes.
- The report should include strengths, problems, improvement suggestions, and an overall score or summary.
- The first version can display the report on the page without saving history.

## Non-Goals

The MVP will not include:

- login or registration
- paid plans
- resume template editing
- complex role matching
- long-term report storage
- admin dashboard

## Basic Quality Requirements

- The project should be runnable locally.
- The project should support Docker Compose deployment later.
- Frontend and backend responsibilities should remain clearly separated.
- Sensitive configuration should be placed in environment variables, not hardcoded.

## Future Considerations

Potential later features include:

- report export
- historical diagnosis records
- job description based comparison
- operations dashboard
- CI/CD pipeline
- monitoring and logging
