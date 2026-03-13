const client = require('prom-client');
const register = new client.Registry();
client.collectDefaultMetrics({ register });

const httpRequestDuration = new client.Histogram({
  name: 'http_request_duration_seconds',
  help: 'Request latency',
  labelNames: ['method', 'route', 'status'],
  registers: [register],
});
const httpRequestTotal = new client.Counter({
  name: 'http_requests_total',
  help: 'Total requests',
  labelNames: ['method', 'route', 'status'],
  registers: [register],
});

function registerMetrics(app) {
  app.use((req, res, next) => {
    const start = Date.now();
    res.on('finish', () => {
      const route = req.route?.path || req.path;
      const status = String(res.statusCode);
      httpRequestDuration.observe({ method: req.method, route, status }, (Date.now() - start) / 1000);
      httpRequestTotal.inc({ method: req.method, route, status });
    });
    next();
  });
}

async function metricsHandler(req, res) {
  res.setHeader('Content-Type', register.contentType);
  res.end(await register.metrics());
}

module.exports = { registerMetrics, metricsHandler, register };
