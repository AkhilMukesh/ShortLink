package com.shortlink.service;

import com.shortlink.dto.CreateUrlRequest;
import com.shortlink.dto.CreateUrlResponse;
import com.shortlink.entity.ShortUrl;
import com.shortlink.exception.DuplicateAliasException;
import com.shortlink.exception.ResourceNotFoundException;
import com.shortlink.repository.ShortUrlRepository;
import com.shortlink.util.ShortCodeGenerator;
import com.shortlink.validation.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlValidator urlValidator;

    public UrlService(
            ShortUrlRepository shortUrlRepository,
            ShortCodeGenerator shortCodeGenerator,
            UrlValidator urlValidator
    ) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.urlValidator = urlValidator;
    }

    @Transactional
    public CreateUrlResponse createUrl(CreateUrlRequest request) {

        urlValidator.validate(request.originalUrl());

        String shortCode;

        if (request.customAlias() != null
                && !request.customAlias().isBlank()) {

            shortCode = request.customAlias().trim();

            if (shortUrlRepository.existsByShortCode(shortCode)) {

                throw new DuplicateAliasException(
                        "Custom alias already exists: " + shortCode
                );
            }

        } else {

            shortCode = generateUniqueShortCode();
        }

        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setShortCode(shortCode);
        shortUrl.setOriginalUrl(request.originalUrl());
        shortUrl.setExpiresAt(request.expiresAt());

        ShortUrl saved = shortUrlRepository.save(shortUrl);

        return toResponse(saved);
    }

    private String generateUniqueShortCode() {

        String code;

        do {
            code = shortCodeGenerator.generate();
        } while (shortUrlRepository.existsByShortCode(code));

        return code;
    }

    @Transactional(readOnly = true)
    public CreateUrlResponse getUrl(Long id) {

        ShortUrl shortUrl = shortUrlRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Short URL not found: " + id
                        )
                );

        return toResponse(shortUrl);
    }

    @Transactional
    public void deactivateUrl(Long id) {

        ShortUrl shortUrl = shortUrlRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Short URL not found: " + id
                        )
                );

        shortUrl.setActive(false);
    }

    private CreateUrlResponse toResponse(ShortUrl shortUrl) {

        String shortUrlValue =
                "http://localhost:8080/"
                        + shortUrl.getShortCode();

        return new CreateUrlResponse(
                shortUrl.getId(),
                shortUrl.getShortCode(),
                shortUrlValue,
                shortUrl.getOriginalUrl(),
                shortUrl.getExpiresAt(),
                shortUrl.isActive()
        );
    }
}