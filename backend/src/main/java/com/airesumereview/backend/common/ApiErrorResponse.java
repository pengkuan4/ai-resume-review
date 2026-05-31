package com.airesumereview.backend.common;

public record ApiErrorResponse(
        String code,
        String message
) {
}
