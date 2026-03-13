const { describe, it } = require('node:test');
const assert = require('node:assert');
const { healthHandler } = require('./health');

describe('health', () => {
  it('returns 200 and status ok', async () => {
    const req = {};
    const res = {
      statusCode: null,
      body: null,
      status(c) { this.statusCode = c; return this; },
      json(o) { this.body = o; return this; },
    };
    await healthHandler(req, res);
    assert.strictEqual(res.statusCode, 200);
    assert.strictEqual(res.body?.status, 'ok');
  });
});
