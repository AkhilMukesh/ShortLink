package com.shortlink.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "short_urls",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_short_urls_short_code",
                        columnNames = "short_code"
                )
        }
)
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "short_code",
            nullable = false,
            length = 50
    )
    private String shortCode;

    @Column(
            name = "original_url",
            nullable = false,
            length = 2048
    )
    private String originalUrl;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(
            name = "is_active",
            nullable = false
    )
    private boolean active = true;

    @Column(
            name = "click_count",
            nullable = false
    )
    private long clickCount = 0;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @PrePersist
    protected void onCreate() {

        Instant now = Instant.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = Instant.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}