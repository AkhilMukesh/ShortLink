package com.shortlink.validation;

import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class UrlValidator {

    public void validate(String url) {

        try {

            URI uri = URI.create(url);

            String scheme = uri.getScheme();

            if (scheme == null ||
                    (!scheme.equalsIgnoreCase("http")
                            && !scheme.equalsIgnoreCase("https"))) {

                throw new IllegalArgumentException(
                        "Only HTTP and HTTPS URLs are allowed"
                );
            }

            if (uri.getHost() == null) {

                throw new IllegalArgumentException(
                        "Invalid URL"
                );
            }

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid URL: " + url
            );
        }
    }
}