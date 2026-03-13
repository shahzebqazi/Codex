-- Schema support for encryption at rest (AES-256). SOC2/GDPR alignment.
-- Run as part of DB init; application layer encrypts/decrypts sensitive columns.
-- Extension for pgcrypto (if available); otherwise app uses AES-256 in application.
CREATE EXTENSION IF EXISTS pgcrypto;

-- Table for user data with placeholder for encrypted columns.
-- Application must encrypt before insert and decrypt after select for PII.
CREATE TABLE IF NOT EXISTS users (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email_enc  BYTEA,
  name_enc   BYTEA,
  tier       TEXT NOT NULL DEFAULT 'free',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS audit_log (
  id         BIGSERIAL PRIMARY KEY,
  user_id    UUID REFERENCES users(id),
  action     TEXT NOT NULL,
  payload_enc BYTEA,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN users.email_enc IS 'AES-256-GCM encrypted email; key from env/secrets';
COMMENT ON COLUMN users.name_enc IS 'AES-256-GCM encrypted display name';
COMMENT ON COLUMN audit_log.payload_enc IS 'AES-256-GCM encrypted payload for PII in audit';
