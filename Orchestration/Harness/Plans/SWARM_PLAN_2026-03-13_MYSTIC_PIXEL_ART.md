# Swarm plan — single PRD: Mystic Pixel Art Rebrand

**Generated:** 2026-03-13
**Mode:** single_prd
**PRD(s):** Orchestration/Harness/PRDs/MYSTIC_PIXEL_ART_SWARM_PRD.md

## Summary

- **Task count:** 4
- **Model/expert assignments:** code (×3), review+shell (×1)
- **Subagent policy:** full

## Task graph

| Id | PRD | Task type | Model | Depends on | Subagent |
|----|-----|-----------|-------|------------|----------|
| PIX-1 | MYSTIC_PIXEL_ART | code | default | none | generalPurpose |
| PAL-1 | MYSTIC_PIXEL_ART | code | default | none | generalPurpose |
| HTM-1 | MYSTIC_PIXEL_ART | code | default | none | generalPurpose |
| INT-1 | MYSTIC_PIXEL_ART | review+shell | default | PIX-1, PAL-1, HTM-1 | shell |

## Tasks (detail)

### PIX-1: Pixel Art Moon and Stars Renderer

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD.md → Agent 1
- **Type:** code
- **Model:** default
- **Subagent:** generalPurpose
- **Depends on:** none
- **Acceptance criteria:** Moon is blocky pixel art with black outline and gold shading. Stars are pixel squares/crosses. Function signatures unchanged.

### PAL-1: Cursor Color Palette Update

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD.md → Agent 2
- **Type:** code
- **Model:** default
- **Subagent:** generalPurpose
- **Depends on:** none
- **Acceptance criteria:** brand.yaml colors section reflects Cursor brand. Moon gold untouched. Hardcoded hex in generators/exporters updated.

### HTM-1: Brand Guide HTML Preview with Twinkle Animation

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD.md → Agent 3
- **Type:** code
- **Model:** default
- **Subagent:** generalPurpose
- **Depends on:** none
- **Acceptance criteria:** index.html has 12 sections including typography, twinkling star animation, and design token reference. Stars are pixel squares.

### INT-1: Integration, Testing, and Asset Generation

- **PRD:** MYSTIC_PIXEL_ART_SWARM_PRD.md → Agent 4
- **Type:** review + shell
- **Model:** default
- **Subagent:** shell
- **Depends on:** PIX-1, PAL-1, HTM-1
- **Acceptance criteria:** Clean build, 45 tests pass, 53 assets generated, preview opens correctly.

## Models used

| model_key | Endpoint / role |
|-----------|------------------|
| default | Cursor agent (claude-4.6-opus-high or equivalent) |

## Execution notes

- Agents 1, 2, 3 have zero file overlap and run in parallel.
- Agent 4 waits for all three, then compiles, tests, fixes, and regenerates.
- All work happens within `Assets/` directory of the repository.
- If Agent 2 changes background hex, Agent 4 must update any test that asserts pixel color values against the old bg.

## Traceability

- Source PRD: Orchestration/Harness/PRDs/MYSTIC_PIXEL_ART_SWARM_PRD.md
- Task graph: this file
- Config: Orchestration/Harness/Plans/SWARM_CONFIG_2026-03-13_MYSTIC_PIXEL_ART.json
