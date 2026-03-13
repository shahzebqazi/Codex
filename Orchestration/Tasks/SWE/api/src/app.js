const express = require('express');
const { errorMiddleware, requestLogger } = require('./middleware/error');
const { featureFlagMiddleware, getFeatureFlags } = require('./middleware/feature-flags');
const { tierGate } = require('./middleware/stripe-tier');
const { registerMetrics, metricsHandler } = require('./lib/metrics');
const { healthHandler } = require('./routes/health');

const app = express();

app.use(requestLogger);
app.use(express.json());

registerMetrics(app);

app.get('/health', healthHandler);
app.get('/metrics', metricsHandler);

app.use(featureFlagMiddleware);

app.get('/api/v1/status', (req, res) => res.json({ status: 'ok', flags: getFeatureFlags() }));

app.get('/api/v1/premium/feature', tierGate(['pro', 'enterprise']), (req, res) => {
  res.json({ message: 'Premium feature', tier: req.userTier });
});

app.use(errorMiddleware);

module.exports = { app };
