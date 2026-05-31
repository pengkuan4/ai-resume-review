package com.airesumereview.backend.resume;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeAnalyzeController {

    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalyzeController(ResumeAnalysisService resumeAnalysisService) {
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/analyze")
    public ResumeAnalysisResponse analyze(@RequestParam("file") MultipartFile file) {
        return resumeAnalysisService.analyze(file);
    }
}
