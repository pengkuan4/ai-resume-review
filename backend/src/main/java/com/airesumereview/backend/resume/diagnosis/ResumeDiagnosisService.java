package com.airesumereview.backend.resume.diagnosis;

public interface ResumeDiagnosisService {

    ResumeDiagnosisResult diagnose(String resumeText);
}
