package com.shortlink.integration;

import com.shortlink.entity.ShortUrl;
import com.shortlink.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    void cleanDatabase() {
        shortUrlRepository.deleteAll();
    }

    @Test
    void shouldCreateShortUrl() throws Exception {

        String requestBody = """
                {
                    "originalUrl": "https://www.google.com"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.shortCode", not(emptyString())))
                .andExpect(jsonPath(
                        "$.originalUrl",
                        is("https://www.google.com")
                ))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void shouldReturnBadRequestForInvalidUrl() throws Exception {

        String requestBody = """
                {
                    "originalUrl": "hello"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUrlById() throws Exception {

        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setShortCode("test123");
        shortUrl.setOriginalUrl("https://www.google.com");

        ShortUrl saved =
                shortUrlRepository.save(shortUrl);

        mockMvc.perform(
                        get("/api/v1/urls/" + saved.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.shortCode",
                        is("test123")
                ))
                .andExpect(jsonPath(
                        "$.originalUrl",
                        is("https://www.google.com")
                ));
    }

    @Test
    void shouldReturnNotFoundForUnknownId() throws Exception {

        mockMvc.perform(
                        get("/api/v1/urls/999999")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectDuplicateAlias() throws Exception {

        String firstRequest = """
                {
                    "originalUrl": "https://google.com",
                    "customAlias": "google"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(firstRequest)
                )
                .andExpect(status().isCreated());

        String secondRequest = """
                {
                    "originalUrl": "https://youtube.com",
                    "customAlias": "google"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(secondRequest)
                )
                .andExpect(status().isConflict());
    }
}