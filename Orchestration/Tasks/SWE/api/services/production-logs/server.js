/**
 * Production-logs service: receives critical failure logs from web tier.
 * Persist to disk or forward to your SIEM; expose /health for orchestration.
 */
const http = require('http');
const fs = require('fs');
const path = require('path');
const PORT = process.env.PORT || 3001;
const LOG_DIR = process.env.LOG_DIR || path.join(__dirname, 'logs');

if (!fs.existsSync(LOG_DIR)) fs.mkdirSync(LOG_DIR, { recursive: true });

function writeLog(entry) {
  const file = path.join(LOG_DIR, `production-${new Date().toISOString().slice(0, 10)}.jsonl`);
  fs.appendFileSync(file, JSON.stringify(entry) + '\n');
}

const server = http.createServer((req, res) => {
  if (req.url === '/health' && req.method === 'GET') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', service: 'production-logs' }));
    return;
  }
  if (req.url === '/ingest' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
      try {
        const entry = JSON.parse(body);
        entry.receivedAt = new Date().toISOString();
        writeLog(entry);
        res.writeHead(204);
        res.end();
      } catch {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: 'Invalid JSON' }));
      }
    });
    return;
  }
  res.writeHead(404);
  res.end();
});

server.listen(PORT, () => console.log(`Production-logs listening on ${PORT}`));
