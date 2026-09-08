package com.shortlink.controller;

import com.shortlink.dto.CreateUrlRequest;
import com.shortlink.dto.CreateUrlResponse;
import com.shortlink.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createUrl(
            @Valid @RequestBody CreateUrlRequest request) {

        CreateUrlResponse response =
                urlService.createUrl(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUrlResponse> getUrl(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                urlService.getUrl(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUrl(
            @PathVariable Long id) {

        urlService.deactivateUrl(id);

        return ResponseEntity.noContent().build();
    }
}