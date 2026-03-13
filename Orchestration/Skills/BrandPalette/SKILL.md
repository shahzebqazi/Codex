# Skill: Brand Palette Migration

**Keyword:** `rebrand`, `palette`, `change colors`, `update brand colors`

**Purpose:** Migrate the Mystic brand color palette by updating `brand.yaml` and propagating changes to any hardcoded values in Kotlin generators, exporters, and tests.

## Steps

1. **Edit `Assets/brand.yaml`** — Replace the `colors:` block with the new palette values. The YAML structure must preserve the existing key hierarchy (`primary`, `primaryDark`, `primaryLight`, `background.*`, `foreground.*`, `light.*`, `accent.*`).

2. **Check generators for hardcoded hex** — Scan the following files for any hardcoded hex color strings that no longer match the palette and update them:
   - `Assets/src/main/kotlin/mystic/assets/generators/IconGenerator.kt` — `svgColorsForTheme()`, `svgBgForTheme()`
   - `Assets/src/main/kotlin/mystic/assets/generators/LogoGenerator.kt` — `svgColorsForTheme()`
   - `Assets/src/main/kotlin/mystic/assets/generators/BannerGenerator.kt`
   - `Assets/src/main/kotlin/mystic/assets/generators/HeroGenerator.kt`

3. **Check exporters** — `CssExporter.kt` and `HtmlExporter.kt` read colors from `config.colors.*` dynamically, so they usually need no changes. Verify no hardcoded hex values remain.

4. **Update tests** — Scan test files for hardcoded hex assertions and update to match the new palette:
   - `AcceptanceTest.kt` — RGB threshold assertions for dark/light backgrounds
   - `CssExporterTest.kt` — CSS variable value assertions
   - `HtmlExporterTest.kt` — swatch/section assertions

5. **Rebuild and verify** — Run `cd Assets && ./gradlew clean build` (all tests must pass), then `./gradlew run` to regenerate assets and visually inspect the output.

## Constraints

- Never change `brand.yaml` keys or structure, only values.
- The `logo.moonGold`, `logo.moonGoldDark`, `logo.moonGoldLight` colors are separate from the UI palette and should not be changed during a palette migration unless explicitly requested.
- The `stars.seed` and star config should remain untouched.
- All color values must be valid 6-digit hex strings prefixed with `#`.

## When to apply

When the user requests a color palette change, brand color update, or asks to match an external brand's colors (e.g., "use Cursor's colors", "change the palette to ...").
