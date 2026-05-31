package com.airesumereview.backend.resume;

import java.util.List;

public record ResumeAnalysisResponse(
        int score,
        String summary,
        List<String> problems,
        List<String> suggestions,
        JobFitResponse jobFit
) {
}
