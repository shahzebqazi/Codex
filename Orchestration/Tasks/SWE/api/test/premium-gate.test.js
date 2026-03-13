const { describe, it } = require('node:test');
const assert = require('node:assert');
const request = require('supertest');
const { app } = require('../src/app');

describe('premium gate', () => {
  it('returns 403 when req.userTier is not set (fail closed)', async () => {
    const res = await request(app)
      .get('/api/v1/premium/feature');
    assert.strictEqual(res.status, 403);
    assert.ok(res.body.error);
    assert.strictEqual(res.body.error, 'Authentication required');
  });

  it('does not trust x-user-tier header', async () => {
    const res = await request(app)
      .get('/api/v1/premium/feature')
      .set('x-user-tier', 'pro');
    assert.strictEqual(res.status, 403);
    assert.strictEqual(res.body.error, 'Authentication required');
  });
});
