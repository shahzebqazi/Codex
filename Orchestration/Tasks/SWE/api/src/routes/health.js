/**
 * Health-check endpoint for LB and orchestrators (Prometheus/Grafana compatible).
 */
const redis = require('../lib/redis');

async function healthHandler(req, res) {
  const checks = { status: 'ok', timestamp: new Date().toISOString() };
  const client = redis.getClient();
  if (client) {
    try {
      await client.ping();
      checks.redis = 'up';
    } catch {
      checks.redis = 'down';
      checks.status = 'degraded';
    }
  } else {
    checks.redis = 'skipped';
  }
  const code = checks.status === 'ok' ? 200 : 503;
  res.status(code).json(checks);
}

module.exports = { healthHandler };
