# GitHub Pages setup

The site is built and deployed by the workflow in `.github/workflows/pages.yml` on every push to `main`.

**To enable the live site:**

1. In the repository on GitHub, go to **Settings → Pages**.
2. Under **Build and deployment**, set **Source** to **GitHub Actions**.

After that, each push to `main` will run the workflow and publish the contents of this `docs/` folder (built with Jekyll) to GitHub Pages. The site URL will be `https://<owner>.github.io/<repo>/` (or your custom domain if configured).

This file is for maintainers; it is not part of the published site (Jekyll excludes files that are not needed for the build).
