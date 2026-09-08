package com.shortlink.controller;

import com.shortlink.service.RedirectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class RedirectController {

    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        String originalUrl =
                redirectService.resolve(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}