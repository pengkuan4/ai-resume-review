package com.airesumereview.backend.resume;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.airesumereview.backend.common.BadRequestException;
import com.airesumereview.backend.resume.diagnosis.ResumeDiagnosisResult;
import com.airesumereview.backend.resume.diagnosis.ResumeDiagnosisService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeAnalysisService {

    private static final int EXTRACTED_TEXT_PREVIEW_LENGTH = 500;

    private final ResumeDiagnosisService resumeDiagnosisService;

    public ResumeAnalysisService(ResumeDiagnosisService resumeDiagnosisService) {
        this.resumeDiagnosisService = resumeDiagnosisService;
    }

    public ResumeAnalysisResponse analyze(MultipartFile file) {
        validatePdfFile(file);

        String extractedText = extractPdfText(file);
        String extractedTextPreview = buildExtractedTextPreview(extractedText);
        ResumeDiagnosisResult diagnosisResult = resumeDiagnosisService.diagnose(extractedText);

        return new ResumeAnalysisResponse(
                diagnosisResult.score(),
                diagnosisResult.summary(),
                extractedTextPreview,
                diagnosisResult.problems(),
                diagnosisResult.suggestions(),
                diagnosisResult.jobFit()
        );
    }

    private void validatePdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("FILE_EMPTY", "The uploaded file must not be empty.");
        }

        if (!hasPdfMetadata(file)) {
            throw new BadRequestException("INVALID_FILE_TYPE", "Only PDF files are supported.");
        }

        if (!hasPdfHeader(file)) {
            throw new BadRequestException("INVALID_PDF_FILE", "The uploaded file is not a valid PDF.");
        }
    }

    private boolean hasPdfMetadata(MultipartFile file) {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        boolean hasPdfContentType = "application/pdf".equalsIgnoreCase(contentType);
        boolean hasPdfExtension = originalFilename != null
                && originalFilename.toLowerCase(Locale.ROOT).endsWith(".pdf");

        return hasPdfContentType || hasPdfExtension;
    }

    private boolean hasPdfHeader(MultipartFile file) {
        try {
            byte[] header = file.getInputStream().readNBytes(5);
            return "%PDF-".equals(new String(header, StandardCharsets.US_ASCII));
        } catch (IOException exception) {
            throw new BadRequestException("FILE_READ_FAILED", "Failed to read the uploaded file.");
        }
    }

    private String extractPdfText(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            String text = new PDFTextStripper().getText(document).trim();

            if (text.isBlank()) {
                throw new BadRequestException(
                        "PDF_TEXT_EMPTY",
                        "No selectable text was found in the PDF. Please upload a text-based resume PDF."
                );
            }

            return text;
        } catch (BadRequestException exception) {
            throw exception;
        } catch (IOException exception) {
            throw new BadRequestException(
                    "PDF_PARSE_FAILED",
                    "Failed to parse PDF text. Please upload a valid text-based PDF resume."
            );
        }
    }

    private String buildExtractedTextPreview(String extractedText) {
        String normalizedText = extractedText.replaceAll("\\s+", " ").trim();

        if (normalizedText.length() <= EXTRACTED_TEXT_PREVIEW_LENGTH) {
            return normalizedText;
        }

        return normalizedText.substring(0, EXTRACTED_TEXT_PREVIEW_LENGTH);
    }
}
