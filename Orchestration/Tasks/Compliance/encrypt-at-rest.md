# Encryption at Rest (AES-256) — SOC2/GDPR

## Approach

- **Database**: Sensitive columns stored as `BYTEA`; application encrypts with AES-256-GCM before write and decrypts after read. Key from secrets (e.g. `ENCRYPTION_KEY` env, 32 bytes for AES-256).
- **Key management**: Rotate keys via key versioning; store active key id in app config. Never log or commit keys.
- **Schema**: See `init-db-encryption.sql` for `users` and `audit_log` tables with `*_enc` columns.

## Script (Node) — encrypt/decrypt helper

Run from application layer when reading/writing PII:

```javascript
// compliance/encrypt-helper.js (use in API only; keep key in env)
const crypto = require('crypto');
const ALGO = 'aes-256-gcm';
const KEY = Buffer.from(process.env.ENCRYPTION_KEY || '', 'hex'); // 32 bytes

function encrypt(plaintext) {
  const iv = crypto.randomBytes(16);
  const cipher = crypto.createCipheriv(ALGO, KEY, iv);
  const enc = Buffer.concat([cipher.update(plaintext, 'utf8'), cipher.final()]);
  const tag = cipher.getAuthTag();
  return Buffer.concat([iv, tag, enc]);
}

function decrypt(buf) {
  const iv = buf.subarray(0, 16);
  const tag = buf.subarray(16, 32);
  const enc = buf.subarray(32);
  const decipher = crypto.createDecipheriv(ALGO, KEY, iv);
  decipher.setAuthTag(tag);
  return decipher.update(enc) + decipher.final('utf8');
}
module.exports = { encrypt, decrypt };
```

## Checklist

- [ ] Set `ENCRYPTION_KEY` (32-byte hex) in production secrets.
- [ ] Use encrypt/decrypt for all PII columns (`email_enc`, `name_enc`, `payload_enc`).
- [ ] Enable DB volume encryption (e.g. EBS encryption, RDS encryption) for rest.
- [ ] Document key rotation procedure and access control.
