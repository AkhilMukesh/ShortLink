CREATE TABLE short_urls (
    id BIGSERIAL PRIMARY KEY,

    short_code VARCHAR(50) NOT NULL,

    original_url VARCHAR(2048) NOT NULL,

    expires_at TIMESTAMPTZ,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    click_count BIGINT NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_short_urls_short_code
        UNIQUE (short_code),

    CONSTRAINT chk_short_urls_click_count
        CHECK (click_count >= 0)
);

CREATE INDEX idx_short_urls_expires_at
ON short_urls(expires_at);