package com.shortlink.service;

import com.shortlink.entity.ShortUrl;
import com.shortlink.exception.ResourceNotFoundException;
import com.shortlink.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RedirectService {

    private final ShortUrlRepository shortUrlRepository;

    public RedirectService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Transactional
    public String resolve(String shortCode) {

        ShortUrl shortUrl =
                shortUrlRepository
                        .findByShortCode(shortCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Short URL not found"
                                )
                        );

        // Check whether URL is active
        if (!shortUrl.isActive()) {

            throw new ResourceNotFoundException(
                    "Short URL is inactive"
            );
        }

        // Check expiration
        if (shortUrl.getExpiresAt() != null
                && shortUrl.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new ResourceNotFoundException(
                    "Short URL has expired"
            );
        }

        // Increment clicks
        shortUrl.setClickCount(
                shortUrl.getClickCount() + 1
        );

        return shortUrl.getOriginalUrl();
    }
}