/**
 * Feature-flag system: Executive team can toggle high-risk features without redeploy.
 * Source: Redis (production) or env/defaults (fallback). DRY single source.
 */
const redis = require('../lib/redis');
const DEFAULT_FLAGS = {
  premium_feature: process.env.FF_PREMIUM_FEATURE !== 'false',
  beta_api: process.env.FF_BETA_API === 'true',
  strict_validation: process.env.FF_STRICT_VALIDATION === 'true',
};

let cachedFlags = { ...DEFAULT_FLAGS };
let lastRefresh = 0;
const TTL_MS = 60 * 1000;

async function loadFlags() {
  if (Date.now() - lastRefresh < TTL_MS) return cachedFlags;
  const client = redis.getClient();
  if (client) {
    try {
      const raw = await client.get('feature_flags');
      if (raw) {
        const parsed = JSON.parse(raw);
        cachedFlags = { ...DEFAULT_FLAGS, ...parsed };
      }
    } catch (_) {
      // keep cached
    }
    lastRefresh = Date.now();
  }
  return cachedFlags;
}

function featureFlagMiddleware(req, res, next) {
  loadFlags().then(flags => {
    req.featureFlags = flags;
    next();
  }).catch(() => {
    req.featureFlags = cachedFlags;
    next();
  });
}

function getFeatureFlags() {
  return { ...cachedFlags };
}

function setFeatureFlag(key, value) {
  cachedFlags[key] = value;
}

module.exports = { featureFlagMiddleware, getFeatureFlags, setFeatureFlag, loadFlags };
