const Redis = require('redis');
const url = process.env.REDIS_URL || 'redis://localhost:6379/0';
let client = null;

async function getClient() {
  if (process.env.NODE_ENV === 'test') return null;
  if (!client) {
    try {
      client = Redis.createClient({ url });
      await client.connect();
    } catch (_) {
      client = null;
    }
  }
  return client;
}

module.exports = { getClient };
