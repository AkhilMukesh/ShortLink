package com.shortlink.dto;

import java.time.Instant;

public record CreateUrlResponse(

        Long id,

        String shortCode,

        String shortUrl,

        String originalUrl,

        Instant expiresAt,

        boolean active
) {
}