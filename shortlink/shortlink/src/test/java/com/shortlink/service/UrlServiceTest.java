package com.shortlink.service;

import com.shortlink.dto.CreateUrlRequest;
import com.shortlink.dto.CreateUrlResponse;
import com.shortlink.entity.ShortUrl;
import com.shortlink.repository.ShortUrlRepository;
import com.shortlink.util.ShortCodeGenerator;
import com.shortlink.validation.UrlValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @Mock
    private UrlValidator urlValidator;

    @InjectMocks
    private UrlService urlService;

    @Test
    void shouldCreateShortUrl() {

        CreateUrlRequest request =
                new CreateUrlRequest(
                        "https://google.com",
                        null,
                        null
                );

        when(shortCodeGenerator.generate())
                .thenReturn("abc1234");

        when(shortUrlRepository.existsByShortCode("abc1234"))
                .thenReturn(false);

        ShortUrl saved = new ShortUrl();

        saved.setShortCode("abc1234");
        saved.setOriginalUrl("https://google.com");

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenReturn(saved);

        CreateUrlResponse response =
                urlService.createUrl(request);

        assertEquals(
                "abc1234",
                response.shortCode()
        );

        assertEquals(
                "https://google.com",
                response.originalUrl()
        );

        verify(shortUrlRepository)
                .save(any(ShortUrl.class));
    }

    @Test
    void shouldReturnExistingUrl() {

        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setShortCode("abc1234");
        shortUrl.setOriginalUrl("https://google.com");

        when(shortUrlRepository.findById(1L))
                .thenReturn(Optional.of(shortUrl));

        CreateUrlResponse response =
                urlService.getUrl(1L);

        assertEquals(
                "abc1234",
                response.shortCode()
        );

        assertEquals(
                "https://google.com",
                response.originalUrl()
        );
    }

    @Test
    void shouldThrowExceptionWhenUrlDoesNotExist() {

        when(shortUrlRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                com.shortlink.exception.ResourceNotFoundException.class,
                () -> urlService.getUrl(999L)
        );

        verify(shortUrlRepository)
                .findById(999L);
    }

    @Test
    void shouldThrowExceptionForDuplicateAlias() {

        CreateUrlRequest request =
                new CreateUrlRequest(
                        "https://google.com",
                        "google",
                        null
                );

        when(shortUrlRepository.existsByShortCode("google"))
                .thenReturn(true);

        assertThrows(
                com.shortlink.exception.DuplicateAliasException.class,
                () -> urlService.createUrl(request)
        );

        verify(shortUrlRepository)
                .existsByShortCode("google");

        verify(shortUrlRepository, never())
                .save(any(ShortUrl.class));
    }
}