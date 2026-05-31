package com.airesumereview.backend.resume.diagnosis;

import java.util.List;

import com.airesumereview.backend.resume.JobFitResponse;

public record ResumeDiagnosisResult(
        int score,
        String summary,
        List<String> problems,
        List<String> suggestions,
        JobFitResponse jobFit
) {
}
