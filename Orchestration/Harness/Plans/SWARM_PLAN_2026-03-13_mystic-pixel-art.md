# Swarm plan — Mystic Pixel Art (single PRD)

**Generated:** 2026-03-13  
**Mode:** single_prd  
**PRD(s):** MYSTIC_PIXEL_ART_SWARM_PRD

## Summary

- **Task count:** 8
- **Model/expert assignments:** code (Kotlin), planning, review
- **Subagent policy:** independent_only

## Task graph

| Id | PRD | Task type | Model | Depends on | Subagent |
|----|-----|-----------|-------|------------|----------|
| PIXEL-1 | MYSTIC_PIXEL_ART_SWARM_PRD | planning | default | none | none |
| PIXEL-2 | MYSTIC_PIXEL_ART_SWARM_PRD | code | code | PIXEL-1 | none |
| PIXEL-3 | MYSTIC_PIXEL_ART_SWARM_PRD | code | code | PIXEL-2 | none |
| PIXEL-4 | MYSTIC_PIXEL_ART_SWARM_PRD | code | code | PIXEL-3 | none |
| PIXEL-5 | MYSTIC_PIXEL_ART_SWARM_PRD | code | code | PIXEL-4 | none |
| PIXEL-6 | MYSTIC_PIXEL_ART_SWARM_PRD | code | code | PIXEL-5 | none |
| PIXEL-7 | MYSTIC_PIXEL_ART_SWARM_PRD | code | code | PIXEL-6 | none |
| PIXEL-8 | MYSTIC_PIXEL_ART_SWARM_PRD | review | review | PIXEL-7 | code-reviewer |

## Tasks (detail)

### PIXEL-1: Config schema and brand.yaml extension

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** planning
- **Model:** default
- **Subagent:** none
- **Depends on:** none
- **Acceptance criteria:** pixelArt section added to brand.yaml with pixelSize, sizes, style; BrandConfig and config classes updated to parse it.

### PIXEL-2: AssetType and Asset model extension

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** code
- **Model:** code
- **Subagent:** none
- **Depends on:** PIXEL-1
- **Acceptance criteria:** PIXEL_ICON and PIXEL_LOGO in AssetType; Asset.subdirectory() handles pixel subdirs; no breaking changes.

### PIXEL-3: PixelArtRenderer implementation

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** code
- **Model:** code
- **Subagent:** none
- **Depends on:** PIXEL-2
- **Acceptance criteria:** PixelArtRenderer renders high-res then downscales with nearest-neighbor; palette from BrandConfig; unit-tested.

### PIXEL-4: PixelIconGenerator

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** code
- **Model:** code
- **Subagent:** none
- **Depends on:** PIXEL-3
- **Acceptance criteria:** PixelIconGenerator produces pixel-art icons for dark/light/mono at configured sizes; reuses ZenMoonRenderer for source.

### PIXEL-5: PixelLogoGenerator

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** code
- **Model:** code
- **Subagent:** none
- **Depends on:** PIXEL-4
- **Acceptance criteria:** PixelLogoGenerator produces pixel-art wordmark and icon-only logos; outputs to logos/pixel/.

### PIXEL-6: Main.kt wiring and export paths

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** code
- **Model:** code
- **Subagent:** none
- **Depends on:** PIXEL-5
- **Acceptance criteria:** Main.kt invokes pixel generators; PngExporter writes to output/icons/pixel/ and output/logos/pixel/.

### PIXEL-7: HtmlExporter pixel section

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** code
- **Model:** code
- **Subagent:** none
- **Depends on:** PIXEL-6
- **Acceptance criteria:** HTML preview includes pixel-art section with side-by-side vector vs pixel comparison.

### PIXEL-8: Tests and review

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD
- **Type:** review
- **Model:** review
- **Subagent:** code-reviewer
- **Depends on:** PIXEL-7
- **Acceptance criteria:** PixelIconGeneratorTest and PixelLogoGeneratorTest pass; palette fidelity and corner pixel assertions; code review complete.

## Models used

| model_key | Endpoint / role |
|-----------|------------------|
| default | Local Ollama / harness default |
| code | Kotlin-focused model |
| review | Code review model |

## Execution notes

- Run tasks PIXEL-1 through PIXEL-7 sequentially; PIXEL-8 can use code-reviewer subagent.
- Assets project uses Gradle; run `./gradlew build` after each code task.
- Config: Orchestration/Harness/Plans/SWARM_CONFIG_2026-03-13_mystic-pixel-art.json

## Traceability

- Source PRD: Orchestration/Harness/PRDs/MYSTIC_PIXEL_ART_SWARM_PRD.md
- Task graph output: (inline in this plan)
- Config: Orchestration/Harness/Plans/SWARM_CONFIG_2026-03-13_mystic-pixel-art.json
