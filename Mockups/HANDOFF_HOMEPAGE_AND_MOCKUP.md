# Handoff: Restore Landing Homepage with Button to Mockup

## Goal

Restore the previous behavior where the **GitHub Pages site had a dedicated landing page (index.html) as the homepage**, and a **button on that page led users to the current Mystic mockup app**. There was an `index.html` (likely on the **assets** branch or used for Pages) that served as this landing page; it is missing or no longer wired, and the site currently serves the mockup app directly at the root.

## Current State

- **Deployment:** The workflow `.github/workflows/deploy-mockups-pages.yml` builds the Mockups app (`Mockups/`, Vite + React) and uploads `Mockups/dist` as the GitHub Pages artifact. The entire site root is the built mockup (title page → app).
- **Site URL:** `https://<owner>.github.io/<repo>/` (e.g. `https://shahzebqazi.github.io/Codex/`).
- **Mockup build:** Uses `BASE_PATH: /${{ github.event.repository.name }}/` so assets and routing work under the project path.
- **Branch context:** The **assets** branch is described in `Orchestration/Harness/Documents/BRANCHES.md` as “Asset and static content (e.g. Pages, koi-pond Mockups).” The landing index.html may have lived there or been used when Pages was deployed from that branch.

## Desired State

1. **Homepage (site root):** A simple **landing page** (single `index.html` or equivalent) that:
   - Is the first thing users see at `https://<owner>.github.io/<repo>/`.
   - Contains at least one **button (or link)** that takes the user to the **current mockup app** (e.g. “Open Mockup”, “Enter Mystic”, “View desktop mockup”).
   - Uses the Mystic brand where appropriate (palette, name). No new colors; keep it minimal.

2. **Mockup app:** The existing Mockups app (title page + desktop UI) should be reachable **after** the user clicks that button, e.g. at a subpath such as:
   - `/<repo>/mockup/` (e.g. `https://shahzebqazi.github.io/Codex/Codex/mockup/` if repo is `Codex`), or
   - `/<repo>/app/`, or another clear subpath you choose.

3. **Deployment:** The Pages artifact must be changed so that:
   - The **root** of the artifact is the **landing** `index.html` (and any assets it needs, e.g. favicon, brand image).
   - The **mockup** lives under a **subpath** (e.g. `mockup/`). That implies:
     - Build the Mockups app with `BASE_PATH` set to that subpath (e.g. `/${{ github.event.repository.name }}/mockup/`).
     - Place the contents of `Mockups/dist` into that subpath in the artifact (e.g. `artifact/mockup/index.html`, `artifact/mockup/assets/...`).
     - The landing page button **href** must point to that subpath (e.g. `./mockup/` or `/<repo>/mockup/`).

## Constraints

- **Landing page:** Can live in the repo at a path you choose, e.g. repo root `index.html`, or `Mockups/static/landing.html` (or `index.html`) that the workflow copies to the artifact root. If you restore from the **assets** branch, check that branch for an existing `index.html` and reuse or adapt it so the single button links to the mockup subpath.
- **Mockups app:** Only change what’s needed so it runs under the new base path (BASE_PATH in the workflow); avoid breaking local dev (`npx vite` with default base `/`).
- **CI/CD:** You may edit `.github/workflows/deploy-mockups-pages.yml` so the artifact is built as: landing `index.html` at root + mockup build output under the chosen subpath.
- **Links:** The landing page must link to the **exact** subpath where the mockup is deployed (same as `BASE_PATH` used for the mockup build), so the button leads to the current mockup.

## Verification

- Opening `https://<owner>.github.io/<repo>/` shows the **landing page** with the button.
- Clicking the button opens the **mockup** (Mystic title page, then desktop mockup) and all assets (scripts, styles, brand images) load correctly.
- Local dev in `Mockups/` still works: `cd Mockups && npx vite` and `npx vite build` (with default or existing base) still run and the app is usable.

## Summary for the AI

1. Create or restore a **landing index.html** at site root with a **button that links to the mockup**.
2. Update **deployment** so the Pages artifact has: root = landing page; mockup app at a **subpath** (e.g. `mockup/`).
3. Set the mockup build **BASE_PATH** to that subpath and ensure the landing button **href** matches it.
4. Keep the Mystic brand and existing Mockups behavior; only add the landing layer and wire the link.
