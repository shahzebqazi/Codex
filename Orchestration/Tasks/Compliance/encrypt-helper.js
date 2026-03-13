/**
 * AES-256-GCM encrypt/decrypt for PII at rest. SOC2/GDPR.
 * Use in API layer only; keep ENCRYPTION_KEY in env/secrets (32-byte hex).
 */
const crypto = require('crypto');
const ALGO = 'aes-256-gcm';
const KEY_LEN = 32;

function getKey() {
  const raw = process.env.ENCRYPTION_KEY || '';
  const buf = Buffer.from(raw, 'hex');
  if (buf.length !== KEY_LEN) return null;
  return buf;
}

function encrypt(plaintext) {
  const key = getKey();
  if (!key) throw new Error('ENCRYPTION_KEY not set or invalid (32-byte hex)');
  const iv = crypto.randomBytes(16);
  const cipher = crypto.createCipheriv(ALGO, key, iv);
  const enc = Buffer.concat([cipher.update(plaintext, 'utf8'), cipher.final()]);
  const tag = cipher.getAuthTag();
  return Buffer.concat([iv, tag, enc]);
}

function decrypt(buf) {
  const key = getKey();
  if (!key) throw new Error('ENCRYPTION_KEY not set or invalid');
  if (!Buffer.isBuffer(buf) || buf.length < 33) throw new Error('Invalid ciphertext');
  const iv = buf.subarray(0, 16);
  const tag = buf.subarray(16, 32);
  const enc = buf.subarray(32);
  const decipher = crypto.createDecipheriv(ALGO, key, iv);
  decipher.setAuthTag(tag);
  return decipher.update(enc) + decipher.final('utf8');
}

module.exports = { encrypt, decrypt, getKey };
