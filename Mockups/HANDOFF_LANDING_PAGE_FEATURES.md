# Handoff: Landing Page — Demo Account, Nerd Integration Buttons & Brand Guide Palette Update

## Goal

Add two new sections to the **Mockups landing page** (the Brand Guide `preview/index.html` on the `assets` branch) and update the **Color Palette** section of the Brand Guide with a new collapsed sub-section for platform integration button colors.

## Current State

- **Landing page:** `Assets/output/preview/index.html` on the **assets** branch. It is a single-file HTML Brand Guide with collapsible `<details>` sections (Overview, Index, Color Palette, Typography, Logos, Icons, Banners, Heroes, SVG Assets, Design Tokens, plus Research sections). It uses Mystic brand CSS variables from `../css/brand.css`.
- **Deployment:** `.github/workflows/deploy-mockups-pages.yml` on `docs/documentation-reorg` fetches `Assets/output/` from the assets branch, copies it into `_site/`, places the Mockups Vite build at `_site/mockups/`, and adds a root redirect `index.html` that sends users to `preview/`.
- **Site structure (deployed):**
  ```
  /<repo>/              → redirect to preview/
  /<repo>/preview/      → Brand Guide (this file)
  /<repo>/mockups/      → Mockups Vite app
  ```
- **Nav bar** already has: `Overview | Brand Guide | Mockups` links.
- **Kotlin generator:** `Assets/` contains a Kotlin toolchain (`./gradlew run`) that generates the brand assets and this HTML page. Changes to `preview/index.html` can be made directly (and regenerated later), or by editing the Kotlin exporter. For this task, **edit the HTML directly** on the assets branch.

## Task 1: Demo User Account Section

### What

Add a **"Demo Account"** section to the landing page — a simulated (fake) user account panel that gives the page a product-like feel. This is purely decorative/mockup; no real auth.

### Requirements

1. Place this section **above** the existing Brand Guide group heading (i.e. above the `<div class="group-heading">Brand Guide</div>` line), but **below** the Index section. Alternatively, add it as a new collapsible `<details class="section">` with its own heading.
2. The section should contain a **mock user card** with:
   - A circular **avatar placeholder** (use a generic silhouette SVG inline, or a pixel-art moon icon from the brand assets at `../icons/pixel/icon-dark-64x64.png`).
   - A **username** (e.g. `mystic-user` or `moonwalker`).
   - A **display name** (e.g. `Luna Devcraft`).
   - A **status badge** — e.g. a small green dot + "Online" or a gold crescent + "Dreaming".
   - A **role/tier label** — e.g. `Free Tier` or `Moonlight Plan` styled as a small badge with `--mystic-accent-gold` background.
3. Below the user card, include **simulated account details** (all fake, just for the mockup):
   - Joined: `March 2025`
   - Plan: `Moonlight (Free)`
   - Storage: `2.4 GB / 5 GB` (maybe a small progress bar using `--mystic-accent-seafoam`)
   - Integrations: `4 connected` (this teases the next section)
4. Style everything with the existing Mystic brand CSS variables. Use the same `card`, `section-content`, and `swatch` patterns already in the page. Dark theme only.
5. The section should be **collapsed by default** (`<details>` without `open`).

## Task 2: Nerd Integration Buttons (Matrix, Mastodon, Nextcloud, XMPP)

### What

Add a set of **platform integration buttons** styled as cards, placed directly below the Demo Account section (or as a sub-section within it). These represent self-hosted / privacy-respecting services that Mystic "connects to."

### Requirements

1. Add **four integration buttons**, each as a styled card/button:

   | Platform   | Icon Source                      | Brand Color  | Label         |
   |------------|----------------------------------|--------------|---------------|
   | **Matrix** | Inline SVG (Matrix logo) or use [Simple Icons](https://simpleicons.org/?q=matrix) | `#0DBD8B`    | Matrix Chat   |
   | **Mastodon** | Inline SVG (Mastodon logo) or Simple Icons | `#6364FF`    | Mastodon      |
   | **Nextcloud** | Inline SVG (Nextcloud logo) or Simple Icons | `#0082C9`  | Nextcloud     |
   | **XMPP**   | Inline SVG (XMPP logo) or Simple Icons | `#002B5C`    | XMPP          |

2. Each button should:
   - Show the **platform icon** (inline SVG preferred for self-contained HTML; ~24×24px, white or the platform color on dark background).
   - Show the **platform name** as a label.
   - Have a **background or left-border accent** using that platform's brand color.
   - Have hover effects consistent with the existing `.card:hover` style (glow, lift).
   - Be a `<button>` or `<a>` that does nothing (no real link needed; `href="#"` or `onclick="return false"` is fine — this is a mockup).
   - Include a **"Connected"** or **"Connect"** status indicator (a small green dot or checkmark for connected, muted for not).
3. Lay them out as a **2×2 grid** on desktop, **single column** on mobile (`grid-template-columns: repeat(auto-fill, minmax(200px, 1fr))`).
4. Optionally add a small heading above like: **Integrations** or **Connected Services** using `h3` style.
5. These buttons should feel like a **nerd's self-hosted dashboard** — privacy-first, open-source vibes. Use `--mystic-font-mono` for labels.

### Icon SVGs

If you need inline SVGs, here are minimal paths for each:

- **Matrix:** `<path d="M.632.55v22.9H2.28V24H0V0h2.28v.55zm7.043 7.26v1.157h.033c.309-.443.683-.784 1.117-1.024.433-.245.936-.365 1.5-.365.54 0 1.036.107 1.49.327.453.218.79.6 1.014 1.142.243-.39.572-.728.986-1.014.415-.286.932-.455 1.55-.455.47 0 .905.054 1.306.17.4.114.744.29 1.032.534.287.24.513.563.67.96.16.397.24.877.24 1.434v5.994h-1.648v-5.093c0-.33-.013-.636-.04-.928a1.858 1.858 0 0 0-.203-.747 1.134 1.134 0 0 0-.467-.5c-.2-.12-.465-.18-.795-.18-.33 0-.6.064-.82.196a1.417 1.417 0 0 0-.49.5 1.963 1.963 0 0 0-.24.68c-.04.248-.063.49-.063.727v5.345h-1.648v-5.093c0-.31-.004-.6-.013-.866a2.146 2.146 0 0 0-.143-.7 1.03 1.03 0 0 0-.415-.5c-.187-.13-.448-.2-.783-.2-.11 0-.258.03-.443.082a1.375 1.375 0 0 0-.507.28 1.6 1.6 0 0 0-.413.567c-.113.24-.17.546-.17.907v5.524h-1.648V7.81zm14.927 0v1.157h.033a2.902 2.902 0 0 1 1.117-1.024c.433-.245.936-.365 1.5-.365.54 0 1.036.107 1.49.327.453.218.79.6 1.014 1.142.243-.39.572-.728.986-1.014.415-.286.932-.455 1.55-.455.47 0 .906.054 1.307.17.4.114.743.29 1.03.534.29.24.514.563.672.96.16.397.24.877.24 1.434v5.994h-1.648v-5.093c0-.33-.014-.636-.04-.928a1.858 1.858 0 0 0-.204-.747 1.134 1.134 0 0 0-.467-.5c-.2-.12-.465-.18-.795-.18-.33 0-.6.064-.82.196a1.417 1.417 0 0 0-.49.5 1.963 1.963 0 0 0-.24.68c-.04.248-.063.49-.063.727v5.345h-1.648v-5.093c0-.31-.004-.6-.013-.866a2.146 2.146 0 0 0-.143-.7 1.03 1.03 0 0 0-.415-.5c-.187-.13-.448-.2-.783-.2-.11 0-.258.03-.443.082a1.375 1.375 0 0 0-.507.28 1.6 1.6 0 0 0-.413.567c-.113.24-.17.546-.17.907v5.524h-1.648V7.81zM23.368.55H24V24h-2.28v-.55h1.648z"/>` (from Simple Icons "matrix" — 24×24 viewBox `0 0 24 24`)
- **Mastodon:** `<path d="M23.268 5.313c-.35-2.578-2.617-4.61-5.304-5.004C17.51.242 15.792 0 11.813 0h-.03c-3.98 0-4.835.242-5.288.309C3.882.692 1.496 2.518.917 5.127.64 6.412.61 7.837.661 9.143c.074 1.874.088 3.745.26 5.611.118 1.24.325 2.47.62 3.68.55 2.237 2.777 4.098 4.96 4.857 2.336.792 4.849.923 7.256.38.265-.061.527-.132.786-.213.585-.184 1.27-.39 1.774-.753a.057.057 0 0 0 .023-.043v-1.809a.052.052 0 0 0-.02-.041.053.053 0 0 0-.046-.01 20.282 20.282 0 0 1-4.709.545c-2.73 0-3.463-1.284-3.674-1.818a5.593 5.593 0 0 1-.319-1.433.053.053 0 0 1 .066-.054 19.648 19.648 0 0 0 4.581.545h.35c1.592 0 3.22-.046 4.759-.353a.058.058 0 0 0 .021-.006c2.263-.44 4.42-1.81 4.634-5.354.008-.152.029-1.579.029-1.735.002-.537.193-3.807-.028-5.825zM19.63 14.05h-2.6v-5.7c0-1.2-.506-1.81-1.52-1.81-1.12 0-1.683.723-1.683 2.153v3.11h-2.584v-3.11c0-1.43-.563-2.153-1.683-2.153-1.014 0-1.52.61-1.52 1.81v5.7H5.46V8.137c0-1.2.304-2.153.92-2.862.63-.71 1.456-1.073 2.486-1.073 1.19 0 2.093.458 2.691 1.373L12 6.36l.443-.785C13.04 4.66 13.944 4.2 15.134 4.2c1.03 0 1.856.364 2.486 1.073.616.71.92 1.663.92 2.862V14.05z"/>` (Simple Icons "mastodon")
- **Nextcloud:** `<path d="M12.018 6.537c-2.5 0-4.6 1.712-5.241 4.015-.56-1.232-1.793-2.105-3.225-2.105A3.569 3.569 0 0 0 0 12.003a3.569 3.569 0 0 0 3.552 3.556c1.432 0 2.664-.874 3.224-2.106.641 2.304 2.742 4.016 5.242 4.016 2.487 0 4.576-1.696 5.229-3.981.563 1.217 1.785 2.071 3.201 2.071A3.569 3.569 0 0 0 24 12.003a3.569 3.569 0 0 0-3.552-3.556c-1.416 0-2.638.854-3.201 2.07-.653-2.284-2.742-3.98-5.229-3.98zm0 2.085c1.984 0 3.39 1.552 3.39 3.381 0 1.83-1.406 3.382-3.39 3.382s-3.39-1.553-3.39-3.382c0-1.83 1.406-3.381 3.39-3.381zM3.552 10.532c.856 0 1.464.66 1.464 1.471s-.608 1.471-1.464 1.471a1.452 1.452 0 0 1-1.467-1.471c0-.81.608-1.471 1.467-1.471zm16.896 0c.858 0 1.467.66 1.467 1.471s-.609 1.471-1.467 1.471c-.856 0-1.464-.66-1.464-1.471s.608-1.471 1.464-1.471z"/>` (Simple Icons "nextcloud")
- **XMPP:** Use the XMPP shield/chat-bubble logo from Simple Icons or a generic chat icon.

You can also use the SVG files hosted at `https://cdn.simpleicons.org/matrix/0DBD8B` etc. if you prefer `<img>` tags over inline SVG. However, **inline SVG is preferred** so the page stays fully self-contained.

## Task 3: Brand Guide — "Buttons" Sub-Section in Color Palette

### What

Add a **second collapsed `<details>` block** inside the existing **Color Palette** section (`#section-color-palette`) of `preview/index.html`, titled **"Buttons — Platform Integration Colors"**. This documents the official brand colors of the four platforms above so designers can reference them.

### Requirements

1. Find the existing Color Palette section in `preview/index.html` (around line 403):
   ```html
   <details class="section" id="section-color-palette">
     <summary>… Color Palette</summary>
     <div class="section-content">
       <div class="swatch-grid">
         … existing swatches …
       </div>
     </div>
   </details>
   ```

2. Inside `.section-content`, **after** the existing `swatch-grid` div, add a nested `<details>` block (collapsed by default) like:
   ```html
   <details style="margin-top: 1.5rem;">
     <summary><h3 style="display:inline; cursor:pointer;">Buttons — Platform Integration Colors</h3></summary>
     <div style="padding: 0.75rem 0;">
       <div class="swatch-grid">
         <!-- new swatches here -->
       </div>
     </div>
   </details>
   ```

3. Add the following **swatches** (using the same `.swatch` class pattern as existing ones):

   | Token / Label                  | Hex       | Text Color |
   |-------------------------------|-----------|------------|
   | `--btn-matrix`                | `#0DBD8B` | `#000`     |
   | `--btn-matrix-dark`           | `#0A9A71` | `#000`     |
   | `--btn-mastodon`              | `#6364FF` | `#fff`     |
   | `--btn-mastodon-dark`         | `#563ACC` | `#fff`     |
   | `--btn-nextcloud`             | `#0082C9` | `#fff`     |
   | `--btn-nextcloud-dark`        | `#006BA1` | `#fff`     |
   | `--btn-xmpp`                  | `#002B5C` | `#fff`     |
   | `--btn-xmpp-light`            | `#008CBA` | `#000`     |

4. Optionally add these tokens to `brand.css` as well (in a `/* Platform Integration Buttons */` comment block) so they can be used from CSS. This is a nice-to-have.

5. Also add corresponding rows to the **Design Tokens Reference** table (`#section-design-tokens`, around line 839) under a new sub-heading row or at the bottom, so the full token set is documented.

## File to Edit

**`Assets/output/preview/index.html`** on the **`assets`** branch. You will need to:
```bash
git checkout assets
# edit Assets/output/preview/index.html
git add Assets/output/preview/index.html
git commit -m "feat(brand-guide): add demo account, integration buttons, button palette"
git push origin assets
```

Then trigger a Pages deploy (push to `docs/documentation-reorg` or `main`, or use `workflow_dispatch`).

## Styling Reference

All styles are inline in the `<style>` block of `preview/index.html`. Key patterns to reuse:

- **Section:** `<details class="section" id="section-xxx">` with `<summary><h2>…</h2></summary>` and `<div class="section-content">…</div>`.
- **Cards:** `.card` class — dark surface, border, rounded, hover glow.
- **Swatches:** `.swatch` class — colored rectangle with label overlay.
- **Grid:** `.grid` or `.swatch-grid` for responsive layouts.
- **Fonts:** `var(--mystic-font-mono)` for technical labels, `var(--mystic-font-body)` for UI text.
- **Colors:** Use existing CSS variables (`--mystic-bg-surface`, `--mystic-fg-border`, `--mystic-accent-*`, etc.).

## Constraints

- **Self-contained:** The page must remain a single HTML file with inline styles. No external JS frameworks.
- **Brand consistency:** Use only existing Mystic brand colors for the page chrome; the platform colors are only for the integration button accents and the new palette sub-section.
- **Collapsed by default:** Both the Demo Account section and the Buttons palette sub-section should be collapsed (`<details>` without `open`).
- **No real functionality:** The demo account and integration buttons are purely visual mockups. No auth, no API calls.
- **Accessibility:** Use semantic HTML, `alt` text on images/icons, sufficient color contrast.

## Verification

1. Open `preview/index.html` locally in a browser (or via the deployed Pages URL).
2. The **Demo Account** section appears (collapsed) — expanding it shows the mock user card with avatar, name, status, plan info, and "4 connected" integrations count.
3. The **Integration Buttons** appear below the user card — four styled cards for Matrix, Mastodon, Nextcloud, XMPP with correct platform icons and colors. Hover effects work.
4. In the **Color Palette** section, a collapsed **"Buttons — Platform Integration Colors"** sub-heading exists. Expanding it shows 8 new swatches with the platform colors.
5. The **Design Tokens** table includes the new `--btn-*` tokens.
6. The page still loads correctly on the deployed site with all existing sections intact.

## Summary for the AI

1. Edit `Assets/output/preview/index.html` on the **assets** branch.
2. Add a **Demo Account** section (collapsed `<details>`) with a mock user card (avatar, username, status, plan, storage bar, integrations count).
3. Add **four integration buttons** (Matrix, Mastodon, Nextcloud, XMPP) as styled cards with inline SVG icons and platform brand colors.
4. Add a **collapsed sub-section** inside Color Palette titled **"Buttons — Platform Integration Colors"** with 8 new color swatches.
5. Add the `--btn-*` tokens to the Design Tokens reference table.
6. Keep all existing content intact; only add new sections.
