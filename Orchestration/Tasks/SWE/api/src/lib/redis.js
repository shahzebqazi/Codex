const Redis = require('redis');
const url = process.env.REDIS_URL || 'redis://localhost:6379/0';
let client = null;

function getClient() {
  if (process.env.NODE_ENV === 'test') return null;
  if (!client) {
    try {
      client = Redis.createClient({ url });
      client.connect().catch(() => { /* connection deferred or failed */ });
    } catch (_) {
      /* createClient failed */
    }
  }
  return client;
}

module.exports = { getClient };
