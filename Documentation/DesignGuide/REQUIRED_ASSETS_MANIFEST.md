# Required Assets Manifest — Design Guide Remake

Assets the GitHub Pages design guide needs for 99designs-aligned sections. Items marked **inline** are produced in HtmlExporter (no separate file). Items marked **optional** can be deferred.

## 1. Design language (terminology, voice & tone)

| Asset | Type | Notes |
|-------|------|--------|
| Terminology glossary | **Inline** | HtmlExporter emits a short glossary (terms: e.g. Mystic, harness, PRD, agent). No image. |
| Voice & tone examples | **Inline** | 2–3 example phrases (headline, body, CTA) in guide copy. No image. |
| Accessibility note (color) | **Inline** | One sentence + link to WCAG or contrast note; can cite existing palette. |

## 2. Layout and navigation

| Asset | Type | Notes |
|-------|------|--------|
| Breakpoints table | **Inline** | HtmlExporter emits a small table: breakpoint name, min-width, use (e.g. mobile, tablet, desktop). |
| Grid/units reference | **Inline** | List or table of spacing units (rem, px) from Design Tokens / brand.css. |
| Navigation behavior (do’s/don’ts) | **Inline** | Bullet list; no image required. **Optional:** simple diagram later. |

## 3. Components and patterns

| Asset | Type | Notes |
|-------|------|--------|
| Button states (default, hover, focus, disabled) | **Inline** | HtmlExporter renders 4 small buttons with CSS classes; no external image. |
| Link states | **Inline** | Same: inline styled links. |
| Card pattern example | **Inline** | Reuse existing `.card` style; one example block in new “Components” section. |
| Form/modal pattern | **Inline** | One minimal example (e.g. input + label + primary button). **Optional:** modal snippet. |

## 4. Data / imagery (optional)

| Asset | Type | Notes |
|-------|------|--------|
| Chart/graph style example | **Optional** | If added: simple inline SVG or CSS bar/line example in guide. |
| Imagery/video guidelines | **Inline** | Short text (aspect ratios, formats, tone). No video file required. |

## 5. Existing assets (no new files)

- All referenced paths in `Assets/output/preview/index.html` (logos, icons, banners, heroes, css) remain under `Assets/output/`. No new image files are required for the new sections above; only new HTML/CSS content in HtmlExporter output.
- The guide’s “Mockups” link targets `../mockups/`, which is correct at deploy time (workflow assembles `_site` with `mockups/` from Mockups/dist). No asset under `Assets/output/mockups` needed.

## 6. External dependencies (unchanged)

- Google Fonts (preconnect + CSS) — keep as-is.
- No new external images or scripts required for the new sections.

## 7. Deployment (GitHub Pages)

- The guide is at **/preview/** (e.g. `https://&lt;owner&gt;.github.io/&lt;repo&gt;/preview/`). The **Mockups** nav link uses `../mockups/` so it resolves to **/mockups/** in the same site.
- The workflow `.github/workflows/deploy-mockups-pages.yml` builds the site: it fetches **Assets/output/** from the **assets** branch, copies it to `_site`, then adds **Mockups/dist** as `_site/mockups`. After changing `HtmlExporter.kt`, run `./gradlew run` in `Assets/` and commit the updated `Assets/output/` (e.g. to the **assets** branch) so the deployed guide includes your changes.
