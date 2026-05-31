package com.airesumereview.backend.resume;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.airesumereview.backend.common.GlobalExceptionHandler;
import com.airesumereview.backend.resume.diagnosis.MockResumeDiagnosisService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResumeAnalyzeController.class)
@Import({ResumeAnalysisService.class, MockResumeDiagnosisService.class, GlobalExceptionHandler.class})
class ResumeAnalyzeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnMockAnalysisAndExtractedTextPreviewForPdfFile() throws Exception {
        MockMultipartFile file = createPdfFile("Java Spring Boot Docker resume");

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(72))
                .andExpect(jsonPath("$.summary").exists())
                .andExpect(jsonPath("$.extractedTextPreview").value("Java Spring Boot Docker resume"))
                .andExpect(jsonPath("$.problems", hasSize(3)))
                .andExpect(jsonPath("$.suggestions", hasSize(3)))
                .andExpect(jsonPath("$.jobFit.backendDevelopment").exists())
                .andExpect(jsonPath("$.jobFit.testDevelopment").exists())
                .andExpect(jsonPath("$.jobFit.devOpsSre").exists());
    }

    @Test
    void shouldLimitExtractedTextPreviewToFiveHundredCharacters() throws Exception {
        MockMultipartFile file = createPdfFile("a".repeat(600));

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.extractedTextPreview", hasLength(lessThanOrEqualTo(500))));
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
                "plain text".getBytes(StandardCharsets.UTF_8)
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
                "not a real pdf".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PDF_FILE"));
    }

    @Test
    void shouldReturnClearMessageWhenPdfParsingFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                "%PDF-1.4\nnot a valid pdf".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/resume/analyze").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PDF_PARSE_FAILED"))
                .andExpect(jsonPath("$.message").value("Failed to parse PDF text. Please upload a valid text-based PDF resume."));
    }

    @Test
    void shouldAllowLocalFrontendOrigin() throws Exception {
        MockMultipartFile file = createPdfFile("Java Spring Boot Docker resume");

        mockMvc.perform(multipart("/api/resume/analyze")
                        .file(file)
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    private MockMultipartFile createPdfFile(String text) throws IOException {
        return new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                createPdfBytes(text)
        );
    }

    private byte[] createPdfBytes(String text) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(text);
                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
