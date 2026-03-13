/**
 * Centralized error handling: log critical failures to production-logs service.
 * DRY: single middleware for all routes.
 */
const LOG_SERVICE_URL = process.env.LOG_SERVICE_URL || '';

async function sendToProductionLogs(level, message, meta = {}) {
  if (!LOG_SERVICE_URL) return;
  try {
    const res = await fetch(`${LOG_SERVICE_URL}/ingest`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        level,
        message,
        timestamp: new Date().toISOString(),
        ...meta,
      }),
    });
    if (!res.ok) console.error('Production-logs ingest failed:', res.status);
  } catch (err) {
    console.error('Production-logs unreachable:', err.message);
  }
}

function requestLogger(req, res, next) {
  const start = Date.now();
  res.on('finish', () => {
    const duration = Date.now() - start;
    if (res.statusCode >= 500) {
      sendToProductionLogs('error', `${req.method} ${req.path} ${res.statusCode}`, {
        duration,
        path: req.path,
        method: req.method,
        statusCode: res.statusCode,
      });
    }
  });
  next();
}

function errorMiddleware(err, req, res, _next) {
  const status = err.statusCode || 500;
  const message = err.message || 'Internal Server Error';
  if (status >= 500) {
    sendToProductionLogs('critical', message, {
      path: req.path,
      method: req.method,
      stack: err.stack,
    });
  }
  res.status(status).json({ error: message });
}

module.exports = { errorMiddleware, requestLogger, sendToProductionLogs };
