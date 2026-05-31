package com.airesumereview.backend.resume;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.airesumereview.backend.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResumeAnalyzeController.class)
@Import({ResumeAnalysisService.class, GlobalExceptionHandler.class})
class ResumeAnalyzeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnMockAnalysisForPdfFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                "%PDF-1.4 mock pdf content".getBytes()
        );

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(72))
                .andExpect(jsonPath("$.summary").exists())
                .andExpect(jsonPath("$.problems", hasSize(3)))
                .andExpect(jsonPath("$.suggestions", hasSize(3)))
                .andExpect(jsonPath("$.jobFit.backendDevelopment").exists())
                .andExpect(jsonPath("$.jobFit.testDevelopment").exists())
                .andExpect(jsonPath("$.jobFit.devOpsSre").exists());
    }

    @Test
    void shouldRejectEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                new byte[0]
        );

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("FILE_EMPTY"));
    }

    @Test
    void shouldRejectNonPdfFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.txt",
                "text/plain",
                "plain text".getBytes()
        );

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_FILE_TYPE"));
    }

    @Test
    void shouldRejectMissingFileField() throws Exception {
        mockMvc.perform(multipart("/api/resume/analyze"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("FILE_REQUIRED"));
    }

    @Test
    void shouldRejectInvalidPdfHeader() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                "not a real pdf".getBytes()
        );

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PDF_FILE"));
    }
}
