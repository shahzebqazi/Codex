# Mystic Pixel Art Rebrand — Agent Swarm PRD

> **For agents:** This is a multi-agent swarm plan. Each numbered agent operates independently on its assigned files. A coordinator agent runs last to integrate, test, and generate final assets. Read your section, execute it, report SUCCESS or BLOCKED.

**Generated:** 2026-03-13
**Mode:** single_prd
**Branch:** `assets` at `/Users/sqazi/cursor-killer/`
**Subagent policy:** full

---

## Goal

Remake the Mystic brand asset generator so that:

1. The **crescent moon** is pixel art — blocky, hand-made feel, with gold fill, internal shading gradients (darker gold toward the inner edge), and a **1px black pixel outline**
2. The **stars** are pixel art — small dot stars are 1-3px squares, sparkle stars are 4-point pixel crosses
3. The **HTML preview** (`index.html`) becomes a comprehensive brand guide containing: all logos, icons, fonts, typography specimens, color palette, design tokens, moon phases, and a **twinkling star animation** (CSS keyframe)
4. All 45 existing tests continue to pass
5. `./gradlew run` regenerates all 53 assets with the new pixel art style

---

## Architecture Summary

| File | Role |
|------|------|
| `Assets/brand.yaml` | Single source of truth: colors, typography, logo geometry, star config |
| `Assets/src/main/kotlin/dotai/assets/config/BrandConfig.kt` | YAML → typed data classes (ColorConfig, LogoConfig, StarConfig, etc.) |
| `Assets/src/main/kotlin/dotai/assets/model/ZenMoonRenderer.kt` | Core drawing engine: moon, stars, phases, wordmark, canvas |
| `Assets/src/main/kotlin/dotai/assets/model/Asset.kt` | Asset data model (type, format, name, dimensions, theme) |
| `Assets/src/main/kotlin/dotai/assets/generators/IconGenerator.kt` | Generates icons (PNG all sizes + SVG, 3 themes) |
| `Assets/src/main/kotlin/dotai/assets/generators/LogoGenerator.kt` | Generates logos (full/icon-only/wordmark × 3 themes + social + watermark + SVGs) |
| `Assets/src/main/kotlin/dotai/assets/generators/BannerGenerator.kt` | Generates banners (github/readme/release) |
| `Assets/src/main/kotlin/dotai/assets/generators/HeroGenerator.kt` | Generates hero images (landing + retina) |
| `Assets/src/main/kotlin/dotai/assets/exporters/PngExporter.kt` | Writes PNG files |
| `Assets/src/main/kotlin/dotai/assets/exporters/SvgExporter.kt` | Writes SVG files |
| `Assets/src/main/kotlin/dotai/assets/exporters/CssExporter.kt` | Generates `brand.css` with CSS custom properties |
| `Assets/src/main/kotlin/dotai/assets/exporters/HtmlExporter.kt` | Generates `preview/index.html` gallery |
| `Assets/src/test/kotlin/dotai/assets/*.kt` | 7 test suites, 45 tests |

### Key invariants

- **Moon gold colors** come from `config.logo.moonGold` / `moonGoldDark` / `moonGoldLight` (YAML `logo:` section). These are `#FAD075`, `#C9A84C`, `#FDE8B0`.
- **UI palette** comes from `config.colors.*` (YAML `colors:` section). Another agent may change these. Do not hardcode UI palette hex — always read from config.
- **Star config** comes from `config.stars.*` (YAML `stars:` section).
- `ZenMoonRenderer` is a Kotlin `object` (singleton) with static methods. All generators call it.
- The signature `moonColorForTheme(colors: ColorConfig, logo: LogoConfig, theme: Theme): Color` selects gold per theme.

---

## Agent Assignments

### AGENT 1: Pixel Moon Renderer

**Type:** code
**Files to modify:** `Assets/src/main/kotlin/dotai/assets/model/ZenMoonRenderer.kt`
**Subagent type:** generalPurpose

**Task:** Rewrite the moon crescent drawing to use pixel art style.

**Replace `drawMoonCrescent()`** with a pixel-art version that:

1. **Disables anti-aliasing** — set `RenderingHints.KEY_ANTIALIASING` to `VALUE_ANTIALIAS_OFF` for the moon drawing. This creates hard pixel edges.
2. **Draws the crescent as filled rectangles** — compute the crescent shape, then rasterize it onto a grid where each "pixel" is a `pixelSize × pixelSize` square. The `pixelSize` should be `max(1, (radius / 16).toInt())` — so a 512px icon gets 12px pixel blocks, a 32px icon gets 2px pixel blocks.
3. **Adds shading** — the inner edge of the crescent (closest to the cutout) should use `moonGoldDark` (`#C9A84C`), the outer edge uses `moonGold` (`#FAD075`), and highlights near the top-left use `moonGoldLight` (`#FDE8B0`). Use a simple distance-from-inner-edge ratio to interpolate between dark and light.
4. **Draws a 1px black outline** — after filling the crescent pixels, draw a 1px (or 1 pixelSize) black (`Color.BLACK`) border around the outer contour of the crescent shape. Only outline pixels that are adjacent to a non-crescent pixel.
5. **Keeps the glow** — the warm ambient glow behind the moon stays (radial gradient), but rendered *before* the pixel moon so it's behind it. The glow should still use anti-aliasing.

**Replace `drawHandDrawnArc()`** — delete it entirely; the pixel outline replaces the wobble effect.

**Replace `drawStarField()`** with a pixel-art version:

1. **Dot stars** become 1×1 or 2×2 filled squares (no anti-aliasing, no ellipses). Use `g.fillRect()` not `Ellipse2D`.
2. **Sparkle stars** (`drawSparkle`) become a pixel cross pattern: a center pixel plus one pixel in each cardinal direction (5 pixels total forming a `+`). Brighter sparkles add diagonal pixels too (9 pixels forming an `×` overlaid on `+`). Use `g.fillRect()` for each pixel.
3. Disable anti-aliasing for all star drawing.

**Keep unchanged:** `drawMoonPhase()`, `drawWordmark()`, `createCanvas()`, `colorForTheme()`, `textColorForTheme()`, `moonColorForTheme()`, `generateMoonSvg()`. The SVG generation stays vector-based.

**Acceptance criteria:**
- Moon crescent is visibly blocky/pixelated at all sizes (especially 128px+ where pixel blocks are obvious)
- Black outline is exactly 1 pixel-block wide around the crescent
- Shading transitions from dark gold (inner) to light gold (outer)
- Stars are sharp rectangles, not smooth circles
- Sparkle stars form a clear pixel cross pattern
- Glow still appears behind the moon on dark themes
- Existing function signatures are unchanged (generators don't need updates)

---

### AGENT 2: Color Palette (Cursor Brand)

**Type:** code
**Files to modify:**
- `Assets/brand.yaml` (colors section only)
- `Assets/src/main/kotlin/dotai/assets/generators/IconGenerator.kt` (hardcoded bg/fg hex in `svgColorsForTheme`, `svgBgForTheme`)
- `Assets/src/main/kotlin/dotai/assets/generators/LogoGenerator.kt` (hardcoded bg/fg/text hex in `svgColorsForTheme`)
- `Assets/src/main/kotlin/dotai/assets/exporters/HtmlExporter.kt` (hardcoded hex in `appendSwatches`, bg hex in `appendMoonPhases`)

**Subagent type:** generalPurpose

**Task:** Research Cursor's (cursor.com) actual brand colors and replace the UI palette.

1. **Research:** Extract Cursor's exact hex values for primary accent, background tiers (darkest, surface, panel, elevated), foreground tiers (primary, secondary, muted, border), light theme, and semantic accents (success, warning, error).
2. **Update `brand.yaml`:** Replace all hex values under `colors:`. Do NOT change `logo:` (moonGold stays), `stars:`, `typography:`, `brand.name`, or any structural keys.
3. **Update hardcoded SVG hex:** In `IconGenerator.kt` and `LogoGenerator.kt`, the `svgColorsForTheme()` and `svgBgForTheme()` functions have hardcoded background/foreground hex. Update those to match the new palette. **Do NOT change moon hex** (`#FAD075`, `#C9A84C`).
4. **Update `HtmlExporter.kt`:** The `appendSwatches()` list has hardcoded hex — update all values. In `appendMoonPhases()`, update the background hex (`#14120B`) to the new darkest background. Keep `#FAD075` unchanged.
5. **Update tests:** If any test asserts a specific hex value that changed, update the expected value. Tests in `CssExporterTest.kt` and `HtmlExporterTest.kt` may need adjustment.

**Acceptance criteria:**
- `brand.yaml` `colors:` section uses Cursor's actual palette
- Moon gold values in `logo:` section are untouched
- All hardcoded hex in generators/exporters match the new palette
- No remnants of old palette (`#F54E00`, `#C43E00`, `#14120B`, etc.) in any file except where they appear as the new Cursor colors

---

### AGENT 3: Brand Guide HTML Preview

**Type:** code
**Files to modify:** `Assets/src/main/kotlin/dotai/assets/exporters/HtmlExporter.kt`
**Subagent type:** generalPurpose

**Task:** Rebuild the HTML preview page into a comprehensive brand guide with twinkling star animation.

The `export()` function generates `preview/index.html`. Rewrite the HTML output to include these sections in order:

1. **Header** — "Mystic Brand Guide" title, tagline, asset count
2. **Color Palette** — existing swatch grid (keep `appendSwatches`)
3. **Typography Specimens** — new section showing:
   - Display font name and specimen ("Mystic" in display font at 48px, 32px, 24px)
   - Body font specimen (tagline text at 16px, 14px, 12px)
   - Mono font specimen (code-like text at 14px)
   - Letter spacing value
   - Font weight variants (400, 500, 700)
4. **Logo Suite** — all logo PNG assets in a grid, grouped by variant (full, icon-only, wordmark) with theme labels
5. **Icon Suite** — all icon PNGs grouped by theme, with size labels
6. **Social Cards** — OG and Twitter cards shown at scaled size
7. **Banners** — existing banner grid
8. **Heroes** — existing hero grid
9. **SVG Assets** — existing SVG grid
10. **Moon Phases** — existing moon phases (keep `appendMoonPhases`)
11. **Twinkling Stars Animation** — a new section with a dark `div` (600×200px) containing CSS-animated twinkling stars. The implementation:
    - Generate 40-60 small `div` elements positioned randomly within the container
    - Each star is a 2×2px or 3×3px square (pixel art style, no border-radius)
    - Each star has a CSS animation: `@keyframes twinkle { 0%, 100% { opacity: 0.2; } 50% { opacity: 1; } }`
    - Each star gets a random `animation-delay` (0s to 4s) and `animation-duration` (1.5s to 3.5s) so they twinkle at different rates
    - Star color is `#FAD075` (moon gold)
    - Include 3-5 "bright" stars that are pixel crosses (5 `div`s arranged in a `+` pattern) with a slightly different animation timing
    - The container background matches `--dotai-bg-dark`
12. **Design Tokens Reference** — a table listing all CSS custom property names, their current values, and usage notes

**Add these CSS rules to the `<style>` block:**
- `@keyframes twinkle` as described
- `.star` — position absolute, background `#FAD075`
- `.star-field` — position relative, overflow hidden, dark background
- `.star-bright` — the pixel cross variant

**Do NOT change:** any non-HTML-exporter file. The function signature `export(assets: List<Asset>, config: BrandConfig): File` stays the same.

**Acceptance criteria:**
- `index.html` contains all 12 sections
- Typography specimens render with correct font families from config
- Twinkling animation plays in the browser (stars visibly fade in/out at different rates)
- Stars in the animation are square (pixel art), not round
- All existing asset images are still displayed
- Moon phases section still present and functional
- Page loads without errors (no broken image refs)

---

### AGENT 4: Integration, Testing, and Asset Generation

**Type:** review + shell
**Files to modify:** Any test file that needs adjustment
**Subagent type:** shell
**Depends on:** AGENT 1, AGENT 2, AGENT 3

**Task:** After the other three agents complete, run the full integration.

1. **Compile:** `cd Assets && ./gradlew clean build --no-daemon`
   - If compilation fails, read the errors and fix them. Common issues: signature mismatches if Agent 1 changed a method signature (they shouldn't), missing imports, hardcoded hex mismatches.
2. **Run tests:** Verify all 45 tests pass across 7 suites.
   - `AcceptanceTest` (13 tests) — may need pixel threshold adjustments for the new dark bg
   - `IconGeneratorTest` (5 tests) — should pass unchanged
   - `LogoGeneratorTest` (6 tests) — should pass unchanged
   - `BannerGeneratorTest` (5 tests) — should pass unchanged
   - `HeroGeneratorTest` (4 tests) — should pass unchanged
   - `CssExporterTest` (6 tests) — may need hex assertion updates
   - `HtmlExporterTest` (6 tests) — should pass; verify "Mystic Brand" and "Moon Phases" assertions still hold
3. **Fix failing tests:** Update expected values, pixel thresholds, or string assertions. Do NOT change test logic or remove tests.
4. **Generate assets:** `cd Assets && ./gradlew run --no-daemon`
5. **Verify output:** Confirm `Assets/output/` contains:
   - `icons/` — 24 PNGs + 3 favicons (pixel art moon visible at 128px+)
   - `logos/` — 9 theme PNGs + 2 social + 1 watermark + 9 SVGs
   - `banners/` — 3 PNGs
   - `heroes/` — 2 PNGs
   - `css/brand.css` — all CSS tokens
   - `preview/index.html` — full brand guide with twinkling animation
6. **Open preview:** `open Assets/output/preview/index.html` and visually confirm:
   - Moon crescents are visibly pixelated with black outlines
   - Stars are pixel squares/crosses
   - Twinkling animation works
   - All sections of the brand guide are present
   - Colors match Cursor's palette

**Acceptance criteria:**
- `./gradlew clean build` exits 0
- All 45 tests pass (0 failures, 0 errors)
- `./gradlew run` exits 0, 53 assets generated
- `preview/index.html` opens and displays correctly

---

## Task Graph

| Id | Agent | Task type | Depends on | Subagent type |
|----|-------|-----------|------------|---------------|
| PIX-1 | Agent 1 | code | none | generalPurpose |
| PAL-1 | Agent 2 | code | none | generalPurpose |
| HTM-1 | Agent 3 | code | none | generalPurpose |
| INT-1 | Agent 4 | review+shell | PIX-1, PAL-1, HTM-1 | shell |

**Parallelism:** Agents 1, 2, and 3 run in parallel (no shared files). Agent 4 runs after all three complete.

```
  ┌─── Agent 1: Pixel Moon ───┐
  │                            │
  ├─── Agent 2: Palette ───────┼──→ Agent 4: Integration
  │                            │
  └─── Agent 3: HTML Guide ───┘
```

---

## Execution

```bash
# After all agents complete:
cd /Users/sqazi/cursor-killer/Assets
./gradlew clean build --no-daemon   # compile + test
./gradlew run --no-daemon           # generate assets
open output/preview/index.html      # visual verification
```
