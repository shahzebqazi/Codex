const { describe, it } = require('node:test');
const assert = require('node:assert');
const { app } = require('../src/app');

describe('app', () => {
  it('exports Express app without binding a port', () => {
    assert.ok(app);
    assert.strictEqual(typeof app.listen, 'function');
  });
});
