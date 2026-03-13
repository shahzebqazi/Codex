const { app } = require('./app');

const PORT = process.env.PORT || 3000;
let server = null;

if (require.main === module) {
  server = app.listen(PORT, () => {
    console.log(`Web tier listening on ${PORT}`);
  });
}

module.exports = { app, server };
