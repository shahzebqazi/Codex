/**
 * Stripe-ready subscription middleware: gates API routes by user tier metadata.
 * Requires req.userId and req.userTier to be set by auth middleware. Fails closed
 * when auth is not present; does not trust client-supplied headers.
 */
function tierGate(allowedTiers = ['pro', 'enterprise']) {
  return (req, res, next) => {
    const tier = req.userTier ? String(req.userTier).toLowerCase() : null;
    if (!tier) {
      res.status(403).json({
        error: 'Authentication required',
        message: 'Subscription tier is set by auth middleware; no client override.',
      });
      return;
    }
    if (allowedTiers.includes(tier)) {
      return next();
    }
    res.status(403).json({
      error: 'Subscription required',
      requiredTiers: allowedTiers,
      currentTier: tier,
    });
  };
}

module.exports = { tierGate };
