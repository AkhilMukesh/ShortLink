package com.shortlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateUrlRequest(

        @NotBlank(message = "Original URL is required")
        @Size(max = 2048, message = "URL cannot exceed 2048 characters")
        @Pattern(
                regexp = "^(https?://).+",
                message = "URL must start with http:// or https://"
        )
        String originalUrl,

        @Size(
                min = 3,
                max = 50,
                message = "Custom alias must contain 3 to 50 characters"
        )
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "Custom alias can contain only letters, numbers, _ and -"
        )
        String customAlias,

        Instant expiresAt
) {
}