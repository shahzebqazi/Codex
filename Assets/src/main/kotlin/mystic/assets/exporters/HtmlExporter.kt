package mystic.assets.exporters

import mystic.assets.config.BrandConfig
import mystic.assets.model.Asset
import mystic.assets.model.AssetFormat
import mystic.assets.model.AssetType
import java.awt.Color
import java.io.File
import kotlin.random.Random

class HtmlExporter(private val outputDir: File) {

    private object Icons {
        private fun svg(body: String) =
            """<svg class="section-icon" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">$body</svg>"""

        val overview    = svg("""<circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/>""")
        val index       = svg("""<line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/>""")
        val moonPhases  = svg("""<path d="M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9Z"/>""")
        val stars       = svg("""<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>""")
        val palette     = svg("""<circle cx="13.5" cy="6.5" r=".5" fill="currentColor"/><circle cx="17.5" cy="10.5" r=".5" fill="currentColor"/><circle cx="8.5" cy="7.5" r=".5" fill="currentColor"/><circle cx="6.5" cy="12" r=".5" fill="currentColor"/><path d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10c.926 0 1.648-.746 1.648-1.688 0-.437-.18-.835-.437-1.125-.29-.289-.438-.652-.438-1.125a1.64 1.64 0 0 1 1.668-1.668h1.996c3.051 0 5.555-2.503 5.555-5.554C21.965 6.012 17.461 2 12 2Z"/>""")
        val typography  = svg("""<polyline points="4 7 4 4 20 4 20 7"/><line x1="9" y1="20" x2="15" y2="20"/><line x1="12" y1="4" x2="12" y2="20"/>""")
        val logo        = svg("""<path d="M6 3h12l4 6-10 13L2 9Z"/>""")
        val pixelLogo   = svg("""<rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/>""")
        val iconSuite   = svg("""<path d="M12 2 2 7l10 5 10-5-10-5Z"/><path d="m2 17 10 5 10-5"/><path d="m2 12 10 5 10-5"/>""")
        val pixelIcon   = svg("""<rect x="2" y="2" width="20" height="20" rx="2"/><path d="M7 2v20"/><path d="M17 2v20"/><path d="M2 12h20"/><path d="M2 7h5"/><path d="M2 17h5"/><path d="M17 7h5"/><path d="M17 17h5"/>""")
        val social      = svg("""<path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8"/><polyline points="16 6 12 2 8 6"/><line x1="12" y1="2" x2="12" y2="15"/>""")
        val banner      = svg("""<rect x="2" y="3" width="20" height="14" rx="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/>""")
        val hero        = svg("""<polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>""")
        val svgAsset    = svg("""<path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14 2 14 8 20 8"/>""")
        val tokens      = svg("""<circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1Z"/>""")
        val research    = svg("""<circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>""")
        val cursorBrand = svg("""<path d="M6.5 6.5 17.5 12 6.5 17.5z"/><rect x="2" y="2" width="20" height="20" rx="2"/>""")
        val brandGuide  = svg("""<path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>""")
        val layoutNav   = svg("""<rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/>""")
        val voiceTone   = svg("""<path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>""")
        val glossary    = svg("""<path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/><line x1="8" y1="6" x2="16" y2="6"/><line x1="8" y1="10" x2="16" y2="10"/>""")
        val components  = svg("""<path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1"/><path d="M12 11v6"/><path d="M9 14h6"/>""")
    }

    fun export(assets: List<Asset>, config: BrandConfig): File {
        val dir = File(outputDir, "preview")
        dir.mkdirs()
        val file = File(dir, "index.html")

        val pngAssets = assets.filter { it.format == AssetFormat.PNG && it.image != null }
        val svgAssets = assets.filter { it.format == AssetFormat.SVG && it.svgContent != null }

        val logoAssets = pngAssets.filter { it.type == AssetType.LOGO }
        val pixelLogoAssets = pngAssets.filter { it.type == AssetType.PIXEL_LOGO }
        val iconAssets = pngAssets.filter { it.type == AssetType.ICON }
        val pixelIconAssets = pngAssets.filter { it.type == AssetType.PIXEL_ICON }
        val socialAssets = pngAssets.filter { it.name.contains("og-") || it.name.contains("twitter-") }
        val bannerAssets = pngAssets.filter { it.type == AssetType.BANNER }
        val heroAssets = pngAssets.filter { it.type == AssetType.HERO }

        val logoImgPath = logoAssets.find { it.name.contains("icon-only") && it.name.contains("dark") && it.width >= 128 }
            ?: logoAssets.find { it.name.contains("dark") }

        val html = buildString {
            appendLine("<!DOCTYPE html>")
            appendLine("""<html lang="en">""")
            appendLine("<head>")
            appendLine("""  <meta charset="UTF-8">""")
            appendLine("""  <meta name="viewport" content="width=device-width, initial-scale=1.0">""")
            appendLine("  <title>${config.name} Brand Guide</title>")
            val fontFamilies = listOf(
                config.typography.display,
                config.typography.body,
                config.typography.serif,
                config.typography.handwriting,
                config.typography.mono
            ).distinct().joinToString("&family=") { it.replace(" ", "+") + ":wght@400;500;700" }
            appendLine("""  <link rel="preconnect" href="https://fonts.googleapis.com">""")
            appendLine("""  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>""")
            appendLine("""  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=$fontFamilies&display=swap">""")
            appendLine("""  <link rel="stylesheet" href="../css/brand.css">""")
            appendStyles()
            appendLine("</head>")
            appendLine("<body>")
            appendLine("""<nav class="top-nav">""")
            appendLine("""  <div class="top-nav-inner">""")
            appendLine("""    <div class="top-nav-links">""")
            appendLine("""      <a href="../">Home</a>""")
            appendLine("""      <a href="#" class="top-nav-active">Design Guide</a>""")
            appendLine("""      <a href="../executive.html">Executive Document</a>""")
            appendLine("""      <a href="../mockups/" class="top-nav-ext">Mockups</a>""")
            appendLine("""    </div>""")
            appendLine("""  </div>""")
            appendLine("""</nav>""")
            appendLine("""<div class="container">""")

            appendLine("""  <header class="brand-header">""")
            if (logoImgPath != null) {
                val relPath = "../${logoImgPath.subdirectory()}/${logoImgPath.filename()}"
                appendLine("""    <img src="$relPath" alt="${config.name} logo" class="header-logo">""")
            }
            appendLine("""    <div class="header-text">""")
            appendLine("      <h1>${config.name}</h1>")
            appendLine("""      <p class="subtitle">${config.tagline}</p>""")
            appendLine("    </div>")
            appendLine("  </header>")

            // ── Overview ──
            appendSection("overview", "${Icons.overview} Overview", open = true) {
                appendLine("""  <div class="overview-content">""")
                appendLine("""    <p>${config.name} is a creative workspace for capturing notes, designing interfaces, and managing products &mdash; all in one place. """ +
                    """It ships as a desktop app with full GUI support, a CLI for automation and scripting, and a plugin architecture for extensibility. """ +
                    """The lo-fi pixel-art brand identity, anchored by a gold crescent moon and twinkling star field, is an invitation to let your imagination run wild.</p>""")
                appendLine("""    <p>This implementation of an AI tool is built around <strong>fun</strong>, <strong>creativity</strong>, <strong>collaboration</strong>, <strong>privacy</strong>, <strong>safety</strong>, and <strong>local-first use</strong>. """ +
                    """Please use AI responsibly.</p>""")
                appendLine("""    <p class="overview-note">All brand assets (icons, logos, banners, heroes, SVGs, CSS tokens, and this preview page) are generated programmatically by the Kotlin toolchain in <code>Assets/</code>. """ +
                    """Run <code>./gradlew run</code> to regenerate the full set of ${assets.size} assets from <code>brand.yaml</code>.</p>""")
                appendLine("  </div>")
            }

            // ── Index ──
            appendSection("index", "${Icons.index} Index", open = true) {
                appendLine("""  <nav class="index-nav">""")
                appendLine("""    <h3>Brand Guide</h3>""")
                appendLine("""    <ul>""")
                appendLine("""      <li><a href="#section-color-palette">${Icons.palette} Color Palette</a></li>""")
                appendLine("""      <li><a href="#section-typography">${Icons.typography} Typography</a></li>""")
                if (logoAssets.isNotEmpty()) appendLine("""      <li><a href="#section-logo-suite">${Icons.logo} Logos</a></li>""")
                if (pixelLogoAssets.isNotEmpty()) appendLine("""      <li><a href="#section-pixel-logos">${Icons.pixelLogo} Pixel Logos</a></li>""")
                if (iconAssets.isNotEmpty()) appendLine("""      <li><a href="#section-icon-suite">${Icons.iconSuite} Icons</a></li>""")
                if (pixelIconAssets.isNotEmpty()) appendLine("""      <li><a href="#section-pixel-icons">${Icons.pixelIcon} Pixel Icons</a></li>""")
                if (socialAssets.isNotEmpty()) appendLine("""      <li><a href="#section-social-cards">${Icons.social} Social Cards</a></li>""")
                if (bannerAssets.isNotEmpty()) appendLine("""      <li><a href="#section-banners">${Icons.banner} Banners</a></li>""")
                if (heroAssets.isNotEmpty()) appendLine("""      <li><a href="#section-heroes">${Icons.hero} Heroes</a></li>""")
                if (svgAssets.isNotEmpty()) appendLine("""      <li><a href="#section-svg-assets">${Icons.svgAsset} SVG Assets</a></li>""")
                appendLine("""      <li><a href="#section-design-tokens">${Icons.tokens} Design Tokens</a></li>""")
                appendLine("""    </ul>""")
                appendLine("""    <h3>Design System</h3>""")
                appendLine("""    <ul>""")
                appendLine("""      <li><a href="#section-voice-tone">${Icons.voiceTone} Voice &amp; Tone</a></li>""")
                appendLine("""      <li><a href="#section-terminology">${Icons.glossary} Terminology</a></li>""")
                appendLine("""      <li><a href="#section-layout-navigation">${Icons.layoutNav} Layout &amp; Navigation</a></li>""")
                appendLine("""      <li><a href="#section-component-states">${Icons.components} Component States</a></li>""")
                appendLine("""    </ul>""")
                appendLine("""    <h3>Research</h3>""")
                appendLine("""    <ul>""")
                appendLine("""      <li><a href="#section-cursor-brand">${Icons.cursorBrand} Cursor Brand</a></li>""")
                appendLine("""      <li><a href="#section-moon-phases">${Icons.moonPhases} Moon Phases</a></li>""")
                appendLine("""      <li><a href="#section-twinkling-stars">${Icons.stars} Twinkling Stars</a></li>""")
                appendLine("""    </ul>""")
                appendLine("""  </nav>""")
            }

            // ── Research ──
            appendLine("""  <div class="group-heading">${Icons.research} Research</div>""")

            appendSection("cursor-brand", "${Icons.cursorBrand} Cursor Brand") {
                appendCursorBrandResearch()
            }

            appendSection("moon-phases", "${Icons.moonPhases} Moon Phases") {
                appendMoonPhases()
            }

            appendSection("twinkling-stars", "${Icons.stars} Twinkling Stars Animation") {
                appendTwinklingStars()
            }

            // ── Brand Guide ──
            appendLine("""  <div class="group-heading">${Icons.brandGuide} Brand Guide</div>""")

            appendSection("color-palette", "${Icons.palette} Color Palette") {
                appendLine("""  <div class="swatch-grid">""")
                appendSwatches(config)
                appendLine("  </div>")
                appendLine("""  <p class="overview-note" style="margin-top:1rem">Text and interactive elements should meet <a href="https://www.w3.org/TR/WCAG21/#contrast-minimum" class="ds-link" target="_blank" rel="noopener">WCAG 2.1 Level AA</a> contrast (4.5:1 for normal text, 3:1 for large). Use gold and seafoam on dark backgrounds for sufficient contrast.</p>""")
            }

            appendSection("typography", "${Icons.typography} Typography Specimens") {
                appendTypography(config)
            }

            if (logoAssets.isNotEmpty()) {
                appendSection("logo-suite", "${Icons.logo} Logo Suite") {
                    val byVariant = logoAssets.groupBy { asset ->
                        val parts = asset.name.split("-").drop(1)
                        parts.firstOrNull() ?: "default"
                    }
                    for ((variant, variantAssets) in byVariant) {
                        appendLine("""  <div class="variant-group">""")
                        appendLine("    <h3>$variant</h3>")
                        appendLine("""    <div class="variant-row">""")
                        for (asset in variantAssets) {
                            val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                            appendLine("""      <div class="card">""")
                            if (asset.name.contains("watermark")) {
                                appendLine("""        <div class="checkerboard"><img src="$relPath" alt="${asset.name}" title="${asset.filename()} (${asset.width}x${asset.height}, ${asset.theme.name.lowercase()})" data-path="$relPath" onclick="showPreview(this)"></div>""")
                            } else {
                                appendLine("""        <img src="$relPath" alt="${asset.name}" title="${asset.filename()} (${asset.width}x${asset.height}, ${asset.theme.name.lowercase()})" data-path="$relPath" onclick="showPreview(this)">""")
                            }
                            appendLine("      </div>")
                        }
                        appendLine("    </div>")
                        appendLine("  </div>")
                    }
                }
            }

            if (pixelLogoAssets.isNotEmpty()) {
                appendSection("pixel-logos", "${Icons.pixelLogo} Pixel Logos") {
                    appendLine("""  <div class="grid">""")
                    for (asset in pixelLogoAssets) {
                        val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                        appendAssetCard(asset, relPath, pixelated = true)
                    }
                    appendLine("  </div>")
                }
            }

            if (iconAssets.isNotEmpty()) {
                appendSection("icon-suite", "${Icons.iconSuite} Icon Suite") {
                    val byTheme = iconAssets.groupBy { it.theme }
                    for ((theme, themeAssets) in byTheme) {
                        appendLine("""  <div class="theme-group">""")
                        appendLine("    <h3>${theme.name.lowercase()}</h3>")
                        appendLine("""    <div class="theme-row">""")
                        for (asset in themeAssets.sortedBy { it.width }) {
                            val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                            appendLine("""      <div class="card">""")
                            appendLine("""        <img src="$relPath" alt="${asset.name}" title="${asset.filename()} (${asset.width}x${asset.height})" style="width:${minOf(asset.width, 128)}px;height:auto" data-path="$relPath" onclick="showPreview(this)">""")
                            appendLine("      </div>")
                        }
                        appendLine("    </div>")
                        appendLine("  </div>")
                    }
                }
            }

            if (pixelIconAssets.isNotEmpty()) {
                appendSection("pixel-icons", "${Icons.pixelIcon} Pixel Icons") {
                    appendLine("""  <div class="grid">""")
                    for (asset in pixelIconAssets) {
                        val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                        appendAssetCard(asset, relPath, pixelated = true)
                    }
                    appendLine("  </div>")
                }
            }

            if (socialAssets.isNotEmpty()) {
                appendSection("social-cards", "${Icons.social} Social Cards") {
                    appendLine("""  <div class="grid">""")
                    for (asset in socialAssets) {
                        val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                        appendLine("""    <div class="card social-card">""")
                        appendLine("""      <img src="$relPath" alt="${asset.name}" title="${asset.filename()} (${asset.width}x${asset.height})" data-path="$relPath" onclick="showPreview(this)">""")
                        appendLine("    </div>")
                    }
                    appendLine("  </div>")
                }
            }

            if (bannerAssets.isNotEmpty()) {
                appendSection("banners", "${Icons.banner} Banners") {
                    appendLine("""  <div class="grid wide-grid">""")
                    for (asset in bannerAssets) {
                        val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                        appendAssetCard(asset, relPath)
                    }
                    appendLine("  </div>")
                }
            }

            if (heroAssets.isNotEmpty()) {
                appendSection("heroes", "${Icons.hero} Heroes") {
                    appendLine("""  <div class="grid wide-grid">""")
                    for (asset in heroAssets) {
                        val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                        appendAssetCard(asset, relPath)
                    }
                    appendLine("  </div>")
                }
            }

            if (svgAssets.isNotEmpty()) {
                appendSection("svg-assets", "${Icons.svgAsset} SVG Assets") {
                    appendLine("""  <div class="grid">""")
                    for (asset in svgAssets) {
                        val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                        appendAssetCard(asset, relPath)
                    }
                    appendLine("  </div>")
                }
            }

            appendSection("design-tokens", "${Icons.tokens} Design Tokens Reference") {
                appendDesignTokens(config)
            }

            // ── Design System (99designs-aligned) ──
            appendLine("""  <div class="group-heading">${Icons.components} Design System</div>""")

            appendSection("voice-tone", "${Icons.voiceTone} Voice & Tone") {
                appendVoiceTone(config)
            }

            appendSection("terminology", "${Icons.glossary} Terminology") {
                appendTerminology(config)
            }

            appendSection("layout-navigation", "${Icons.layoutNav} Layout & Navigation") {
                appendLayoutNavigation(config)
            }

            appendSection("component-states", "${Icons.components} Component States") {
                appendComponentStates(config)
            }

            appendLightbox()
            appendScript()

            appendLine("</div>")
            appendLine("</body>")
            appendLine("</html>")
        }

        file.writeText(html)
        return file
    }

    private fun StringBuilder.appendSection(id: String, title: String, open: Boolean = false, content: StringBuilder.() -> Unit) {
        val openAttr = if (open) " open" else ""
        appendLine("""  <details class="section" id="section-$id"$openAttr>""")
        appendLine("""    <summary><h2 style="display:inline">$title</h2></summary>""")
        appendLine("""    <div class="section-content">""")
        content()
        appendLine("    </div>")
        appendLine("  </details>")
    }

    private fun StringBuilder.appendAssetCard(asset: Asset, relPath: String, pixelated: Boolean = false) {
        val pixelStyle = if (pixelated) " style=\"image-rendering:pixelated\"" else ""
        appendLine("""    <div class="card">""")
        if (asset.name.contains("watermark")) {
            appendLine("""      <div class="checkerboard"><img src="$relPath" alt="${asset.name}" title="${asset.filename()} (${asset.width}x${asset.height})"$pixelStyle data-path="$relPath" onclick="showPreview(this)"></div>""")
        } else {
            appendLine("""      <img src="$relPath" alt="${asset.name}" title="${asset.filename()} (${asset.width}x${asset.height})"$pixelStyle data-path="$relPath" onclick="showPreview(this)">""")
        }
        appendLine("    </div>")
    }

    private fun StringBuilder.appendStyles() {
        appendLine("  <style>")
        appendLine("    @keyframes twinkle { 0%, 100% { opacity: 0.15; } 50% { opacity: 1; } }")
        appendLine("    @keyframes fadeIn { from { opacity: 0; transform: translateY(6px); } to { opacity: 1; transform: translateY(0); } }")
        appendLine("    @keyframes shimmer { 0% { background-position: -200% 0; } 100% { background-position: 200% 0; } }")
        appendLine("    * { margin: 0; padding: 0; box-sizing: border-box; }")
        appendLine("    ::selection { background: var(--mystic-accent-gold); color: #000; }")
        appendLine("    ::-webkit-scrollbar { width: 8px; }")
        appendLine("    ::-webkit-scrollbar-track { background: var(--mystic-bg-dark); }")
        appendLine("    ::-webkit-scrollbar-thumb { background: var(--mystic-fg-border); border-radius: 4px; }")
        appendLine("    ::-webkit-scrollbar-thumb:hover { background: var(--mystic-fg-muted); }")
        appendLine("    body {")
        appendLine("      background: var(--mystic-bg-dark);")
        appendLine("      color: var(--mystic-fg-primary);")
        appendLine("      font-family: var(--mystic-font-body);")
        appendLine("      padding: 1rem;")
        appendLine("      padding-top: 3.5rem;")
        appendLine("      line-height: 1.6;")
        appendLine("    }")

        // ── Top Navigation ──
        appendLine("    .top-nav { position: fixed; top: 0; left: 0; right: 0; z-index: 50; background: rgba(10,10,11,0.85); border-bottom: 1px solid var(--mystic-fg-border); backdrop-filter: blur(16px) saturate(1.6); -webkit-backdrop-filter: blur(16px) saturate(1.6); }")
        appendLine("    .top-nav-inner { max-width: 1200px; margin: 0 auto; padding: 0 1rem; display: flex; align-items: center; justify-content: center; height: 2.75rem; }")
        appendLine("    .top-nav-brand { display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: var(--mystic-fg-primary); font-family: var(--mystic-font-display); font-size: 0.9rem; font-weight: 500; letter-spacing: var(--mystic-tracking); transition: color 0.15s; }")
        appendLine("    .top-nav-brand:hover { color: var(--mystic-accent-gold); }")
        appendLine("    .top-nav-logo { width: 22px; height: 22px; object-fit: contain; }")
        appendLine("    .top-nav-links { display: flex; align-items: center; gap: 1.25rem; }")
        appendLine("    .top-nav-links a { text-decoration: none; color: var(--mystic-fg-muted); font-size: 0.8rem; font-family: var(--mystic-font-body); transition: color 0.2s; }")
        appendLine("    .top-nav-links a:hover { color: var(--mystic-accent-gold); }")
        appendLine("    .top-nav-links a.top-nav-active { color: var(--mystic-accent-gold); font-weight: 500; }")
        appendLine("    .top-nav-links a.top-nav-ext { padding: 0.25rem 0.7rem; border: 1px solid var(--mystic-fg-border); border-radius: 6px; font-size: 0.75rem; transition: all 0.2s; }")
        appendLine("    .top-nav-links a.top-nav-ext:hover { border-color: var(--mystic-accent-gold); color: var(--mystic-accent-gold); background: rgba(250,208,117,0.06); }")

        // ── Layout ──
        appendLine("    .container { max-width: 1200px; margin: 0 auto; padding: 0 1rem; }")

        // ── Typography ──
        appendLine("    h1 { font-size: 2.5rem; margin-bottom: 0.25rem; letter-spacing: var(--mystic-tracking); font-family: var(--mystic-font-display); background: linear-gradient(135deg, var(--mystic-accent-gold), var(--mystic-primary-light)); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }")
        appendLine("    h2 { font-size: 1.25rem; color: var(--mystic-fg-primary); margin: 0; }")
        appendLine("    h3 { font-size: 1rem; color: var(--mystic-accent-gold); margin-bottom: 0.75rem; text-transform: capitalize; }")
        appendLine("    .subtitle { color: var(--mystic-fg-muted); margin: 0; font-size: 0.95rem; }")

        // ── Brand Header ──
        appendLine("    .brand-header { display: flex; align-items: center; gap: 1.5rem; margin-bottom: 2rem; padding: 2rem 0; animation: fadeIn 0.5s ease; }")
        appendLine("    .header-logo { width: 80px; height: 80px; object-fit: contain; flex-shrink: 0; }")
        appendLine("    .header-text { display: flex; flex-direction: column; }")

        // ── Overview ──
        appendLine("    .overview-content { line-height: 1.8; font-size: 0.95rem; color: var(--mystic-fg-secondary, var(--mystic-fg-primary)); }")
        appendLine("    .overview-content p { margin-bottom: 0.75rem; }")
        appendLine("    .overview-note { padding: 0.85rem 1.1rem; background: var(--mystic-bg-surface); border-left: 3px solid var(--mystic-accent-gold); border-radius: 0 6px 6px 0; font-size: 0.85rem; }")
        appendLine("    .overview-note code { font-family: var(--mystic-font-mono); color: var(--mystic-accent-seafoam); background: var(--mystic-bg-panel); padding: 0.15rem 0.4rem; border-radius: 3px; font-size: 0.8rem; }")

        // ── Group Headings ──
        appendLine("    .group-heading { font-size: 0.7rem; text-transform: uppercase; letter-spacing: 0.18em; color: var(--mystic-accent-gold); margin: 2.5rem 0 0.35rem; padding-bottom: 0.35rem; border-bottom: 2px solid var(--mystic-fg-border); font-family: var(--mystic-font-mono); display: flex; align-items: center; gap: 0.4rem; }")

        // ── Index ──
        appendLine("    .index-nav h3 { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.12em; color: var(--mystic-accent-lavender); margin: 0.75rem 0 0.35rem; font-family: var(--mystic-font-mono); }")
        appendLine("    .index-nav ul { list-style: none; padding: 0; margin: 0 0 0.5rem; display: flex; flex-wrap: wrap; gap: 0.3rem 1.25rem; }")
        appendLine("    .index-nav a { color: var(--mystic-accent-gold); text-decoration: none; font-size: 0.85rem; transition: color 0.15s; }")
        appendLine("    .index-nav a:hover { color: var(--mystic-accent-rose); text-decoration: underline; }")

        // ── Sections (collapsible) ──
        appendLine("    details.section { border-bottom: 1px solid var(--mystic-fg-border); margin-top: 0.5rem; }")
        appendLine("    details.section > summary { cursor: pointer; padding: 0.85rem 0; list-style: none; display: flex; align-items: center; gap: 0.5rem; transition: color 0.15s; }")
        appendLine("    details.section > summary:hover h2 { color: var(--mystic-accent-gold); }")
        appendLine("    details.section > summary::-webkit-details-marker { display: none; }")
        appendLine("    details.section > summary::before { content: '\\25B6'; font-size: 0.65rem; color: var(--mystic-accent-gold); transition: transform 0.2s; }")
        appendLine("    details.section[open] > summary::before { transform: rotate(90deg); }")
        appendLine("    .section-content { padding: 0.75rem 0 1.75rem; animation: fadeIn 0.3s ease; }")
        appendLine("    .section-icon { width: 18px; height: 18px; vertical-align: -3px; flex-shrink: 0; color: var(--mystic-accent-gold); }")
        appendLine("    .group-heading .section-icon { width: 14px; height: 14px; vertical-align: -2px; }")
        appendLine("    .index-nav .section-icon { width: 14px; height: 14px; vertical-align: -2px; }")
        appendLine("    details.section > summary:hover .section-icon { color: var(--mystic-accent-rose); }")

        // ── Cards & Grids ──
        appendLine("    .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 1rem; }")
        appendLine("    .card {")
        appendLine("      background: var(--mystic-bg-surface);")
        appendLine("      border: 1px solid var(--mystic-fg-border);")
        appendLine("      border-radius: 10px;")
        appendLine("      padding: 0.75rem;")
        appendLine("      display: flex;")
        appendLine("      flex-direction: column;")
        appendLine("      align-items: center;")
        appendLine("      cursor: pointer;")
        appendLine("      transition: all 0.2s ease;")
        appendLine("    }")
        appendLine("    .card:hover { border-color: var(--mystic-accent-gold); box-shadow: 0 0 16px rgba(250,208,117,0.08), 0 4px 12px rgba(0,0,0,0.3); transform: translateY(-2px); }")
        appendLine("    .card img { max-width: 100%; height: auto; border-radius: 4px; }")
        appendLine("    .wide-grid { grid-template-columns: 1fr; }")
        appendLine("    .wide-grid .card img { max-height: 300px; object-fit: contain; }")
        appendLine("    .checkerboard { background: repeating-conic-gradient(#1a1a1a 0% 25%, #0a0a0a 0% 50%) 0 0 / 20px 20px; padding: 0.75rem; border-radius: 6px; }")

        // ── Swatches ──
        appendLine("    .swatch-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 0.85rem; }")
        appendLine("    .swatch { height: 72px; border-radius: 10px; display: flex; align-items: flex-end; padding: 0.4rem 0.55rem; font-size: 0.6rem; font-family: var(--mystic-font-mono); transition: transform 0.2s, box-shadow 0.2s; box-shadow: inset 0 -24px 20px -12px rgba(0,0,0,0.25); }")
        appendLine("    .swatch:hover { transform: scale(1.06); box-shadow: 0 4px 16px rgba(0,0,0,0.4), inset 0 -24px 20px -12px rgba(0,0,0,0.25); }")
        appendLine("    .social-card { max-width: 400px; }")
        appendLine("    .social-card img { max-width: 100%; height: auto; }")

        // ── Typography Specimens ──
        appendLine("    .type-specimen {")
        appendLine("      background: var(--mystic-bg-surface);")
        appendLine("      border: 1px solid var(--mystic-fg-border);")
        appendLine("      border-radius: 10px;")
        appendLine("      padding: 1.25rem;")
        appendLine("      margin-bottom: 1rem;")
        appendLine("      transition: border-color 0.2s;")
        appendLine("    }")
        appendLine("    .type-specimen:hover { border-color: var(--mystic-accent-lavender); }")
        appendLine("    .type-specimen .specimen-label {")
        appendLine("      font-size: 0.7rem; color: var(--mystic-fg-muted); font-family: var(--mystic-font-mono);")
        appendLine("      margin-top: 0.75rem; border-top: 1px solid var(--mystic-fg-border); padding-top: 0.5rem;")
        appendLine("    }")
        appendLine("    .type-role { font-size: 0.78rem; color: var(--mystic-fg-muted); font-family: var(--mystic-font-mono); margin: -0.3rem 0 0.6rem; font-style: italic; }")
        appendLine("    .type-row { display: flex; gap: 1.5rem; flex-wrap: wrap; }")
        appendLine("    .type-row .type-specimen { flex: 1; min-width: 200px; }")

        // ── Variant/Theme Groups ──
        appendLine("    .variant-group { margin-bottom: 1.5rem; }")
        appendLine("    .variant-row { display: flex; gap: 1rem; flex-wrap: wrap; align-items: flex-end; }")
        appendLine("    .theme-group { margin-bottom: 1.5rem; }")
        appendLine("    .theme-row { display: flex; gap: 0.75rem; flex-wrap: wrap; align-items: center; }")

        // ── Token Table ──
        appendLine("    .token-table { width: 100%; border-collapse: collapse; font-size: 0.85rem; }")
        appendLine("    .token-table th { text-align: left; padding: 0.6rem 0.85rem; border-bottom: 2px solid var(--mystic-accent-gold); color: var(--mystic-accent-gold); font-weight: 500; font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em; }")
        appendLine("    .token-table td { padding: 0.55rem 0.85rem; border-bottom: 1px solid var(--mystic-fg-border); transition: background 0.15s; }")
        appendLine("    .token-table tr:hover td { background: var(--mystic-bg-surface); }")
        appendLine("    .token-table td:first-child { font-family: var(--mystic-font-mono); font-size: 0.8rem; color: var(--mystic-accent-seafoam); }")
        appendLine("    .token-table td:nth-child(2) { font-family: var(--mystic-font-mono); font-size: 0.8rem; }")
        appendLine("    .token-swatch { display: inline-block; width: 16px; height: 16px; border-radius: 4px; vertical-align: middle; margin-right: 0.4rem; border: 1px solid var(--mystic-fg-border); box-shadow: 0 1px 3px rgba(0,0,0,0.3); }")

        // ── Lightbox ──
        appendLine("    #lightbox { position: fixed; inset: 0; background: rgba(0,0,0,0.92); display: none; z-index: 100; flex-direction: column; align-items: center; justify-content: center; backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); }")
        appendLine("    #lightbox.active { display: flex; }")
        appendLine("    #lightbox img { max-width: 90vw; max-height: 75vh; object-fit: contain; border-radius: 10px; image-rendering: auto; box-shadow: 0 8px 40px rgba(0,0,0,0.6); }")
        appendLine("    #lightbox .lb-info { margin-top: 1.25rem; text-align: center; font-family: var(--mystic-font-mono); font-size: 0.85rem; color: var(--mystic-fg-muted); }")
        appendLine("    #lightbox .lb-download { display: inline-block; margin-top: 0.5rem; padding: 0.45rem 1.5rem; background: linear-gradient(135deg, var(--mystic-accent-gold), var(--mystic-primary)); color: #000; border-radius: 8px; text-decoration: none; font-size: 0.8rem; font-weight: 600; transition: opacity 0.2s, transform 0.2s; }")
        appendLine("    #lightbox .lb-download:hover { opacity: 0.9; transform: translateY(-1px); }")
        appendLine("    #lightbox .lb-close { position: absolute; top: 1rem; right: 1.5rem; font-size: 2rem; color: var(--mystic-fg-muted); cursor: pointer; background: none; border: none; transition: color 0.15s; }")
        appendLine("    #lightbox .lb-close:hover { color: var(--mystic-accent-rose); }")

        // ── Research Prose ──
        appendLine("    .research-prose { line-height: 1.75; color: var(--mystic-fg-secondary); font-family: var(--mystic-font-serif); }")
        appendLine("    .research-prose h3 { color: var(--mystic-accent-gold); font-size: 0.95rem; font-family: var(--mystic-font-mono); text-transform: uppercase; letter-spacing: 0.08em; margin: 1.5rem 0 0.5rem; }")
        appendLine("    .research-prose p { margin: 0.6rem 0; font-size: 0.88rem; }")
        appendLine("    .research-prose strong { color: var(--mystic-fg-primary); }")
        appendLine("    .research-intro { font-size: 0.95rem !important; color: var(--mystic-fg-primary); border-left: 3px solid var(--mystic-accent-gold); padding-left: 1rem; margin-bottom: 1.25rem !important; }")
        appendLine("    .research-table { width: 100%; border-collapse: collapse; font-size: 0.82rem; margin: 0.75rem 0 1rem; }")
        appendLine("    .research-table th { text-align: left; padding: 0.5rem 0.75rem; border-bottom: 2px solid var(--mystic-accent-gold); color: var(--mystic-accent-gold); font-weight: 500; font-size: 0.72rem; text-transform: uppercase; letter-spacing: 0.06em; font-family: var(--mystic-font-mono); }")
        appendLine("    .research-table td { padding: 0.45rem 0.75rem; border-bottom: 1px solid var(--mystic-fg-border); }")
        appendLine("    .research-table tr:hover td { background: var(--mystic-bg-surface); }")
        appendLine("    .research-table code { font-family: var(--mystic-font-mono); font-size: 0.78rem; color: var(--mystic-accent-seafoam); background: var(--mystic-bg-surface); padding: 0.1rem 0.35rem; border-radius: 4px; }")
        appendLine("    .research-list { padding-left: 1.5rem; margin: 0.5rem 0; }")
        appendLine("    .research-list li { margin: 0.35rem 0; font-size: 0.88rem; }")
        appendLine("    .research-list li strong { color: var(--mystic-fg-primary); }")

        // ── Star Animations ──
        appendLine("    .star-field { position: relative; overflow: hidden; }")
        appendLine("    .star { position: absolute; background: var(--mystic-accent-gold); animation-name: twinkle; animation-iteration-count: infinite; animation-timing-function: ease-in-out; border-radius: 50%; }")
        appendLine("    .star-bright { position: absolute; animation: twinkle 2s ease-in-out infinite; }")

        // ── Design System sections ──
        appendLine("    .ds-glossary { padding: 0; margin: 0; }")
        appendLine("    .ds-glossary dt { font-family: var(--mystic-font-mono); color: var(--mystic-accent-seafoam); font-size: 0.85rem; margin-top: 0.75rem; margin-bottom: 0.2rem; }")
        appendLine("    .ds-glossary dt:first-child { margin-top: 0; }")
        appendLine("    .ds-glossary dd { margin: 0 0 0 1rem; color: var(--mystic-fg-secondary); font-size: 0.9rem; padding-bottom: 0.5rem; border-bottom: 1px solid var(--mystic-fg-border); }")
        appendLine("    .ds-breakpoints-table { width: 100%; border-collapse: collapse; font-size: 0.85rem; margin: 0.75rem 0; }")
        appendLine("    .ds-breakpoints-table th { text-align: left; padding: 0.5rem 0.75rem; border-bottom: 2px solid var(--mystic-accent-gold); color: var(--mystic-accent-gold); font-weight: 500; font-size: 0.72rem; text-transform: uppercase; letter-spacing: 0.06em; font-family: var(--mystic-font-mono); }")
        appendLine("    .ds-breakpoints-table td { padding: 0.45rem 0.75rem; border-bottom: 1px solid var(--mystic-fg-border); }")
        appendLine("    .ds-breakpoints-table tr:hover td { background: var(--mystic-bg-surface); }")
        appendLine("    .ds-state-row { display: flex; flex-wrap: wrap; gap: 1rem; align-items: center; margin-bottom: 1rem; }")
        appendLine("    .ds-state-label { font-size: 0.75rem; color: var(--mystic-fg-muted); font-family: var(--mystic-font-mono); margin-bottom: 0.25rem; }")
        appendLine("    .ds-btn-primary { padding: 0.5rem 1rem; background: linear-gradient(135deg, var(--mystic-accent-gold), var(--mystic-primary)); color: #000; border: none; border-radius: 8px; font-family: var(--mystic-font-body); font-size: 0.875rem; font-weight: 500; cursor: pointer; transition: opacity 0.2s, transform 0.2s; }")
        appendLine("    .ds-btn-primary:hover { opacity: 0.9; transform: translateY(-1px); }")
        appendLine("    .ds-btn-primary:disabled { opacity: 0.4; cursor: not-allowed; transform: none; }")
        appendLine("    .ds-btn-secondary { padding: 0.5rem 1rem; background: transparent; color: var(--mystic-accent-gold); border: 1px solid var(--mystic-fg-border); border-radius: 8px; font-family: var(--mystic-font-body); font-size: 0.875rem; cursor: pointer; transition: border-color 0.2s, color 0.2s; }")
        appendLine("    .ds-btn-secondary:hover { border-color: var(--mystic-accent-gold); color: var(--mystic-primary-light); }")
        appendLine("    .ds-link { color: var(--mystic-accent-lavender); text-decoration: none; font-size: 0.9rem; transition: color 0.15s; }")
        appendLine("    .ds-link:hover { color: var(--mystic-accent-gold); text-decoration: underline; }")
        appendLine("    .ds-link:focus { outline: 2px solid var(--mystic-accent-gold); outline-offset: 2px; }")
        appendLine("    .ds-dos-donts { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-top: 0.75rem; }")
        appendLine("    .ds-dos-donts .do { padding: 0.75rem; background: rgba(94, 196, 171, 0.08); border-left: 3px solid var(--mystic-accent-seafoam); border-radius: 0 6px 6px 0; font-size: 0.85rem; }")
        appendLine("    .ds-dos-donts .dont { padding: 0.75rem; background: rgba(232, 114, 138, 0.06); border-left: 3px solid var(--mystic-accent-rose); border-radius: 0 6px 6px 0; font-size: 0.85rem; }")
        appendLine("    @media (max-width: 600px) { .ds-dos-donts { grid-template-columns: 1fr; } }")

        // ── Responsive ──
        appendLine("    @media (max-width: 768px) {")
        appendLine("      h1 { font-size: 1.75rem; }")
        appendLine("      .top-nav-brand span { display: none; }")
        appendLine("      .top-nav-links { gap: 0.75rem; }")
        appendLine("      .top-nav-links a { font-size: 0.7rem; }")
        appendLine("      .brand-header { flex-direction: column; text-align: center; padding: 1.25rem 0; }")
        appendLine("      .header-logo { width: 64px; height: 64px; }")
        appendLine("      .grid { grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 0.75rem; }")
        appendLine("      .swatch-grid { grid-template-columns: repeat(auto-fill, minmax(95px, 1fr)); }")
        appendLine("      .type-row .type-specimen { min-width: 100%; }")
        appendLine("      .token-table { font-size: 0.75rem; }")
        appendLine("      .token-table td, .token-table th { padding: 0.35rem 0.5rem; }")
        appendLine("    }")
        appendLine("  </style>")
    }

    private fun StringBuilder.appendLightbox() {
        appendLine("""  <div id="lightbox" onclick="closeLightbox(event)">""")
        appendLine("""    <button class="lb-close" onclick="closeLightbox(event)">&times;</button>""")
        appendLine("""    <img id="lb-img" src="" alt="Preview">""")
        appendLine("""    <div class="lb-info">""")
        appendLine("""      <div id="lb-name"></div>""")
        appendLine("""      <a id="lb-link" class="lb-download" href="" download>Download</a>""")
        appendLine("    </div>")
        appendLine("  </div>")
    }

    private fun StringBuilder.appendScript() {
        appendLine("<script>")
        appendLine("const STORAGE_KEY = 'mystic-brand-guide-sections';")
        appendLine("function loadSections() {")
        appendLine("  try {")
        appendLine("    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY) || '{}');")
        appendLine("    document.querySelectorAll('details.section').forEach(d => {")
        appendLine("      if (saved[d.id] === true) d.open = true;")
        appendLine("    });")
        appendLine("  } catch(e) {}")
        appendLine("}")
        appendLine("function saveSections() {")
        appendLine("  const state = {};")
        appendLine("  document.querySelectorAll('details.section').forEach(d => {")
        appendLine("    state[d.id] = d.open;")
        appendLine("  });")
        appendLine("  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(state)); } catch(e) {}")
        appendLine("}")
        appendLine("document.addEventListener('DOMContentLoaded', loadSections);")
        appendLine("document.addEventListener('toggle', e => { if (e.target.matches('details.section')) saveSections(); }, true);")
        appendLine("function showPreview(el) {")
        appendLine("  const src = el.getAttribute('data-path') || el.src;")
        appendLine("  const name = el.getAttribute('title') || el.alt || '';")
        appendLine("  document.getElementById('lb-img').src = src;")
        appendLine("  document.getElementById('lb-name').textContent = name;")
        appendLine("  const link = document.getElementById('lb-link');")
        appendLine("  link.href = src;")
        appendLine("  link.download = src.split('/').pop();")
        appendLine("  document.getElementById('lightbox').classList.add('active');")
        appendLine("}")
        appendLine("function closeLightbox(e) {")
        appendLine("  if (e.target.id === 'lightbox' || e.target.classList.contains('lb-close')) {")
        appendLine("    document.getElementById('lightbox').classList.remove('active');")
        appendLine("  }")
        appendLine("}")
        appendLine("document.addEventListener('keydown', e => { if (e.key === 'Escape') document.getElementById('lightbox').classList.remove('active'); });")
        appendLine("</script>")
    }

    private fun StringBuilder.appendTypography(config: BrandConfig) {
        appendLine("""  <h3>Display &mdash; ${config.typography.display}</h3>""")
        appendLine("""  <p class="type-role">Headings, titles, hero text</p>""")
        appendLine("""  <div class="type-row">""")
        for ((size, label) in listOf(48 to "48px", 32 to "32px", 24 to "24px")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-display);font-size:${size}px;letter-spacing:var(--mystic-tracking);line-height:1.2">${config.name}</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.display} &middot; Display / $label</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")

        appendLine("""  <h3>Body &mdash; ${config.typography.body}</h3>""")
        appendLine("""  <p class="type-role">UI text, menus, descriptions</p>""")
        appendLine("""  <div class="type-row">""")
        for ((size, label) in listOf(16 to "16px", 14 to "14px", 12 to "12px")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-body);font-size:${size}px;line-height:1.5">${config.tagline}</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.body} &middot; Body / $label</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")

        appendLine("""  <h3>Serif &mdash; ${config.typography.serif}</h3>""")
        appendLine("""  <p class="type-role">Long-form notes, prose, articles</p>""")
        appendLine("""  <div class="type-row">""")
        for ((size, label) in listOf(20 to "20px", 16 to "16px", 14 to "14px")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-serif);font-size:${size}px;line-height:1.7">The quick brown fox jumps over the lazy dog. Notes become ideas, ideas become projects.</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.serif} &middot; Serif / $label</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")

        appendLine("""  <h3>Handwriting &mdash; ${config.typography.handwriting}</h3>""")
        appendLine("""  <p class="type-role">Personal notes, annotations, sketches</p>""")
        appendLine("""  <div class="type-row">""")
        for ((size, label) in listOf(28 to "28px", 22 to "22px", 18 to "18px")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-handwriting);font-size:${size}px;line-height:1.5">Remember to sketch the wireframe before coding!</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.handwriting} &middot; Handwriting / $label</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")

        appendLine("""  <h3>Mono &mdash; ${config.typography.mono}</h3>""")
        appendLine("""  <p class="type-role">Code, tokens, terminal, technical labels</p>""")
        appendLine("""  <div class="type-specimen" style="max-width:600px">""")
        appendLine("""    <div style="font-family:var(--mystic-font-mono);font-size:14px;line-height:1.6;color:var(--mystic-accent-seafoam)">val greeting = "Hello, ${config.name}"<br>fun fibonacci(n: Int): Int =<br>&nbsp;&nbsp;if (n &lt;= 1) n else fibonacci(n-1) + fibonacci(n-2)</div>""")
        appendLine("""    <div class="specimen-label">${config.typography.mono} &middot; Mono / 14px &middot; tracking: ${config.typography.tracking}em</div>""")
        appendLine("  </div>")

        appendLine("""  <h3>Weight Ramp</h3>""")
        appendLine("""  <div class="type-row" style="margin-top:0.5rem">""")
        for ((weight, label) in listOf(400 to "Regular", 500 to "Medium", 700 to "Bold")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-display);font-size:24px;font-weight:$weight;letter-spacing:var(--mystic-tracking)">${config.name}</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.display} &middot; $weight ($label)</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")
    }

    private fun StringBuilder.appendTwinklingStars() {
        appendLine("""  <div class="star-field" style="width:100%;height:200px;position:relative;overflow:hidden;background:var(--mystic-bg-dark);border-radius:8px;border:1px solid var(--mystic-fg-border)">""")
        val rng = Random(42)
        for (i in 0 until 50) {
            val left = rng.nextDouble(0.0, 100.0)
            val top = rng.nextDouble(0.0, 100.0)
            val size = if (rng.nextBoolean()) 2 else 3
            val delay = rng.nextDouble(0.0, 4.0)
            val duration = rng.nextDouble(1.5, 3.5)
            appendLine("""    <div class="star" style="left:${"%.1f".format(left)}%;top:${"%.1f".format(top)}%;width:${size}px;height:${size}px;animation-delay:${"%.1f".format(delay)}s;animation-duration:${"%.1f".format(duration)}s"></div>""")
        }
        for (i in 0 until 4) {
            val left = rng.nextDouble(5.0, 90.0)
            val top = rng.nextDouble(10.0, 85.0)
            appendLine("""    <div class="star-bright" style="left:${"%.1f".format(left)}%;top:${"%.1f".format(top)}%">""")
            for (dy in listOf(-3, 0, 3)) {
                for (dx in listOf(-3, 0, 3)) {
                    if (dx == 0 || dy == 0) {
                        appendLine("""      <div style="position:absolute;left:${dx}px;top:${dy}px;width:3px;height:3px;background:#FAD075"></div>""")
                    }
                }
            }
            appendLine("    </div>")
        }
        appendLine("  </div>")
    }

    private fun StringBuilder.appendDesignTokens(config: BrandConfig) {
        appendLine("""  <table class="token-table">""")
        appendLine("    <thead><tr><th>Token Name</th><th>Value</th><th>Usage</th></tr></thead>")
        appendLine("    <tbody>")
        val tokens = listOf(
            Triple("--mystic-primary", hex(config.colors.primary), "Primary brand color"),
            Triple("--mystic-primary-dark", hex(config.colors.primaryDark), "Darker primary for hover/active states"),
            Triple("--mystic-primary-light", hex(config.colors.primaryLight), "Lighter primary for highlights"),
            Triple("--mystic-bg-dark", hex(config.colors.backgroundDark), "Page background (dark theme)"),
            Triple("--mystic-bg-surface", hex(config.colors.backgroundSurface), "Card and surface background"),
            Triple("--mystic-bg-panel", hex(config.colors.backgroundPanel), "Side panels and navigation"),
            Triple("--mystic-bg-elevated", hex(config.colors.backgroundElevated), "Elevated surfaces (modals, dropdowns)"),
            Triple("--mystic-fg-primary", hex(config.colors.foregroundPrimary), "Primary text color"),
            Triple("--mystic-fg-secondary", hex(config.colors.foregroundSecondary), "Secondary text color"),
            Triple("--mystic-fg-muted", hex(config.colors.foregroundMuted), "Muted text, captions"),
            Triple("--mystic-fg-border", hex(config.colors.foregroundBorder), "Borders and dividers"),
            Triple("--mystic-light-bg", hex(config.colors.lightBackground), "Page background (light theme)"),
            Triple("--mystic-light-fg", hex(config.colors.lightForeground), "Primary text (light theme)"),
            Triple("--mystic-light-muted", hex(config.colors.lightMuted), "Muted text (light theme)"),
            Triple("--mystic-light-surface", hex(config.colors.lightSurface), "Surface background (light theme)"),
            Triple("--mystic-accent-gold", hex(config.colors.accentGold), "Warm highlights, star accents"),
            Triple("--mystic-accent-rose", hex(config.colors.accentRose), "Alerts, attention, emphasis"),
            Triple("--mystic-accent-seafoam", hex(config.colors.accentSeafoam), "Success states, positive actions"),
            Triple("--mystic-accent-lavender", hex(config.colors.accentLavender), "Interactive elements, links"),
            Triple("--mystic-accent-ivory", hex(config.colors.accentIvory), "Subtle highlights, cards"),
            Triple("--mystic-font-display", "'${config.typography.display}'", "Headings and display text"),
            Triple("--mystic-font-body", "'${config.typography.body}'", "Body text, UI, menus"),
            Triple("--mystic-font-serif", "'${config.typography.serif}'", "Long-form notes, prose, articles"),
            Triple("--mystic-font-handwriting", "'${config.typography.handwriting}'", "Personal notes, annotations"),
            Triple("--mystic-font-mono", "'${config.typography.mono}'", "Code, tokens, technical labels"),
            Triple("--mystic-tracking", "${config.typography.tracking}em", "Default letter-spacing")
        )
        for ((name, value, usage) in tokens) {
            val swatchHtml = if (value.startsWith("#")) """<span class="token-swatch" style="background:$value"></span>""" else ""
            appendLine("      <tr><td>$name</td><td>$swatchHtml$value</td><td>$usage</td></tr>")
        }
        appendLine("    </tbody>")
        appendLine("  </table>")
    }

    private fun StringBuilder.appendVoiceTone(config: BrandConfig) {
        appendLine("""  <p class="overview-content">Use a clear, warm, and inclusive voice. Be concise in UI; allow personality in marketing and notes.</p>""")
        appendLine("""  <h3>Examples</h3>""")
        appendLine("""  <div class="type-specimen">""")
        appendLine("""    <div class="ds-state-label">Headline</div>""")
        appendLine("""    <div style="font-family:var(--mystic-font-display);font-size:1.5rem;color:var(--mystic-fg-primary)">${config.name} &mdash; Notes, Code, Design</div>""")
        appendLine("""  </div>""")
        appendLine("""  <div class="type-specimen">""")
        appendLine("""    <div class="ds-state-label">Body</div>""")
        appendLine("""    <div style="font-family:var(--mystic-font-body);font-size:0.95rem;line-height:1.6;color:var(--mystic-fg-secondary)">We believe machines need human beings to know what is true and what is wrong. Please use AI responsibly.</div>""")
        appendLine("""  </div>""")
        appendLine("""  <div class="type-specimen">""")
        appendLine("""    <div class="ds-state-label">CTA</div>""")
        appendLine("""    <div style="font-family:var(--mystic-font-body);font-size:0.9rem;color:var(--mystic-accent-gold)">Enter ${config.name} &rarr;</div>""")
        appendLine("""  </div>""")
    }

    private fun StringBuilder.appendTerminology(config: BrandConfig) {
        appendLine("""  <p class="overview-content">Terms used consistently across ${config.name} docs and UI.</p>""")
        appendLine("""  <dl class="ds-glossary">""")
        val terms = listOf(
            config.name to "The creative workspace product (notes, code, design).",
            "Harness" to "Framework that controls how agents run: same inputs, tools, and evaluation.",
            "PRD" to "Product Requirements Document; defines scope and acceptance criteria.",
            "Agent" to "AI system that takes multiple steps (read, edit, run tools) to reach a goal.",
            "Design system" to "Centralized documentation of UI components and brand for reuse."
        )
        for ((term, def) in terms) {
            appendLine("""    <dt>$term</dt>""")
            appendLine("""    <dd>$def</dd>""")
        }
        appendLine("""  </dl>""")
    }

    private fun StringBuilder.appendLayoutNavigation(config: BrandConfig) {
        appendLine("""  <h3>Breakpoints</h3>""")
        appendLine("""  <p class="subtitle" style="margin-bottom:0.5rem">Use these to keep layout and navigation consistent across devices.</p>""")
        appendLine("""  <table class="ds-breakpoints-table">""")
        appendLine("""    <thead><tr><th>Name</th><th>Min width</th><th>Use</th></tr></thead>""")
        appendLine("""    <tbody>""")
        appendLine("""      <tr><td>Mobile</td><td>0</td><td>Single column, full-width tap targets</td></tr>""")
        appendLine("""      <tr><td>Tablet</td><td>768px</td><td>Optional sidebar, grid layouts</td></tr>""")
        appendLine("""      <tr><td>Desktop</td><td>1024px</td><td>Full layout, sidebars, multi-column</td></tr>""")
        appendLine("""      <tr><td>Wide</td><td>1280px</td><td>Max content width ~1200px, centered</td></tr>""")
        appendLine("""    </tbody>""")
        appendLine("""  </table>""")
        appendLine("""  <h3>Units</h3>""")
        appendLine("""  <p class="overview-content">Spacing: <code>rem</code> for typography and padding (1rem = 16px base). Use design tokens (e.g. <code>--mystic-tracking</code>) for consistency.</p>""")
        appendLine("""  <h3>Navigation</h3>""")
        appendLine("""  <div class="ds-dos-donts">""")
        appendLine("""    <div class="do"><strong>Do:</strong> Keep primary nav in a fixed top bar; use clear labels; highlight active section.</div>""")
        appendLine("""    <div class="dont"><strong>Don't:</strong> Nest more than two levels; use vague labels; hide primary actions.</div>""")
        appendLine("""  </div>""")
    }

    private fun StringBuilder.appendComponentStates(config: BrandConfig) {
        appendLine("""  <p class="overview-content">Button and link states must be visible and consistent. Use these patterns in UI.</p>""")
        appendLine("""  <h3>Buttons</h3>""")
        appendLine("""  <div class="ds-state-row">""")
        appendLine("""    <div><div class="ds-state-label">Default</div><button type="button" class="ds-btn-primary">Primary</button></div>""")
        appendLine("""    <div><div class="ds-state-label">Hover / focus</div><button type="button" class="ds-btn-primary" style="opacity:0.9">Primary</button></div>""")
        appendLine("""    <div><div class="ds-state-label">Disabled</div><button type="button" class="ds-btn-primary" disabled>Primary</button></div>""")
        appendLine("""  </div>""")
        appendLine("""  <div class="ds-state-row">""")
        appendLine("""    <div><div class="ds-state-label">Secondary default</div><button type="button" class="ds-btn-secondary">Secondary</button></div>""")
        appendLine("""  </div>""")
        appendLine("""  <h3>Links</h3>""")
        appendLine("""  <div class="ds-state-row">""")
        appendLine("""    <div><div class="ds-state-label">Default</div><a href="#section-overview" class="ds-link">Overview</a></div>""")
        appendLine("""    <div><div class="ds-state-label">Hover</div><a href="#section-overview" class="ds-link" style="color:var(--mystic-accent-gold);text-decoration:underline">Overview</a></div>""")
        appendLine("""  </div>""")
        appendLine("""  <h3>Cards</h3>""")
        appendLine("""  <p class="subtitle" style="margin-bottom:0.5rem">Use the same <code>.card</code> pattern as in this guide: surface background, border, rounded corners, hover lift.</p>""")
        appendLine("""  <div class="card" style="max-width:280px">""")
        appendLine("""    <div style="font-family:var(--mystic-font-display);font-size:1rem;margin-bottom:0.25rem">Card title</div>""")
        appendLine("""    <div style="font-size:0.85rem;color:var(--mystic-fg-muted)">Short description or metadata.</div>""")
        appendLine("""  </div>""")
    }

    private fun StringBuilder.appendCursorBrandResearch() {
        appendLine("""  <div class="research-prose">""")
        appendLine("""    <p class="research-intro">""")
        appendLine("""      Mystic's visual identity draws inspiration from <strong>Cursor</strong>&mdash;the AI-first code editor by Anysphere&mdash;while""")
        appendLine("""      adding its own personality and color. This section documents the reference design system we studied""")
        appendLine("""      and the deliberate departures we made to give Mystic a warmer, more expressive character.""")
        appendLine("""    </p>""")

        appendLine("""    <h3>Typography</h3>""")
        appendLine("""    <p>""")
        appendLine("""      Cursor's 2025 brand refresh introduced <strong>Cursor Gothic</strong>, a custom typeface designed by""")
        appendLine("""      Munich-based foundry <em>Kimera</em> in collaboration with Justin Jay Wang. Built on Kimera's""")
        appendLine("""      <em>Waldenburg</em>, it blends the rationalism of Akzidenz-Grotesk with the analog warmth of Univers.""")
        appendLine("""      Slightly more condensed and higher-contrast than its parent, it reads smoothly at both display and""")
        appendLine("""      text sizes. A distinctive feature is <strong>logo ligatures</strong> baked into the font itself&mdash;""")
        appendLine("""      embedding logo lockups that auto-center with surrounding text.""")
        appendLine("""    </p>""")
        appendLine("""    <p>""")
        appendLine("""      Cursor also pairs the typeface with <strong>Berkeley Mono</strong> for code surfaces and""")
        appendLine("""      <strong>JJannon</strong> (a serif) for editorial content, giving it three distinct typographic voices.""")
        appendLine("""    </p>""")

        appendLine("""    <h3>Color System</h3>""")
        appendLine("""    <table class="research-table">""")
        appendLine("""      <thead><tr><th>Role</th><th>Light</th><th>Dark</th><th>Notes</th></tr></thead>""")
        appendLine("""      <tbody>""")
        appendLine("""        <tr><td>Background</td><td><code>#F7F7F4</code></td><td><code>#14120B</code></td><td>Warm off-white / warm charcoal&mdash;never pure black or white</td></tr>""")
        appendLine("""        <tr><td>Foreground</td><td><code>#26251E</code></td><td><code>#EDECEC</code></td><td>55% opacity for body text, full opacity for headings</td></tr>""")
        appendLine("""        <tr><td>Border / Muted</td><td><code>rgba(38,37,30,0.08)</code></td><td><code>rgba(237,236,236,0.08)</code></td><td>Transparent borders keep depth subtle</td></tr>""")
        appendLine("""        <tr><td>Accent (CTA)</td><td colspan="2"><code>rgba(38,37,30,0.06)</code></td><td>Tinted backgrounds rather than saturated buttons</td></tr>""")
        appendLine("""      </tbody>""")
        appendLine("""    </table>""")

        appendLine("""    <h3>Logo System</h3>""")
        appendLine("""    <p>""")
        appendLine("""      The Cursor logo extends Ben Barry's original cube design into three rendering tiers:""")
        appendLine("""    </p>""")
        appendLine("""    <ul class="research-list">""")
        appendLine("""      <li><strong>2D flat</strong>&mdash;single-color, optimized for small sizes and favicons</li>""")
        appendLine("""      <li><strong>2.5D</strong>&mdash;flat color planes for medium applications (social cards, headers)</li>""")
        appendLine("""      <li><strong>3D</strong>&mdash;lighting and material effects for app icons and OS integrations</li>""")
        appendLine("""    </ul>""")
        appendLine("""    <p>""")
        appendLine("""      Subtle rounding on cube and cursor-arrow corners ensures screen clarity at every size.""")
        appendLine("""      Each tier ships in light and dark variants.""")
        appendLine("""    </p>""")

        appendLine("""    <h3>Design Philosophy</h3>""")
        appendLine("""    <p>""")
        appendLine("""      Cursor positions itself as a <em>"power tool for professional developers"</em>,""")
        appendLine("""      balancing high-end technical sophistication with human warmth. Every visual decision""")
        appendLine("""      prioritizes <strong>screen fidelity over geometric perfection</strong>&mdash;shapes are""")
        appendLine("""      optically tuned rather than mathematically exact. The overall palette is restrained,""")
        appendLine("""      almost monochromatic, relying on warm neutrals and transparency.""")
        appendLine("""    </p>""")

        appendLine("""    <h3>Mystic's Spin</h3>""")
        appendLine("""    <p>""")
        appendLine("""      Where Cursor keeps the palette austere, <strong>Mystic leans into personality and color</strong>.""")
        appendLine("""      We retain the readability-first hierarchy&mdash;dark backgrounds, generous whitespace,""")
        appendLine("""      clear type&mdash;but inject a hand-crafted, lo-fi warmth:""")
        appendLine("""    </p>""")
        appendLine("""    <ul class="research-list">""")
        appendLine("""      <li><strong>Gold accent</strong> replaces the neutral CTA tint, evoking moonlight and craft</li>""")
        appendLine("""      <li><strong>Pixel-art icons &amp; moon</strong> add tactile character versus vector minimalism</li>""")
        appendLine("""      <li><strong>Rose Pink &amp; Seafoam Green</strong> accents give the palette emotional range</li>""")
        appendLine("""      <li><strong>Twinkling star fields</strong> create a living, ambient background layer</li>""")
        appendLine("""      <li><strong>Hard-banded shading</strong> on the crescent moon nods to retro game art</li>""")
        appendLine("""    </ul>""")
        appendLine("""    <p>""")
        appendLine("""      The goal is to keep what makes Cursor's design work&mdash;the readability,""")
        appendLine("""      the restraint, the focus on the craft of code&mdash;while giving Mystic its own""")
        appendLine("""      expressive, imaginative voice.""")
        appendLine("""    </p>""")

        appendLine("""  </div>""")
    }

    private fun StringBuilder.appendMoonPhases() {
        val phases = listOf(
            "New Moon" to 0.0, "Waxing Crescent" to 0.125, "First Quarter" to 0.25,
            "Waxing Gibbous" to 0.375, "Full Moon" to 0.5, "Waning Gibbous" to 0.625,
            "Last Quarter" to 0.75, "Waning Crescent" to 0.875
        )
        appendLine("""  <p class="subtitle" style="margin-bottom:1rem">All eight lunar phases rendered with the Mystic gold palette</p>""")
        appendLine("""  <div class="grid" style="grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));">""")
        for ((name, phase) in phases) {
            val size = 80; val r = size * 0.4; val cx = size / 2.0; val cy = size / 2.0
            val svg = buildString {
                append("""<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 $size $size">""")
                append("""<circle cx="$cx" cy="$cy" r="${r * 1.15}" fill="#FAD075" opacity="0.06"/>""")
                append("""<circle cx="$cx" cy="$cy" r="$r" fill="#FAD075"/>""")
                if (phase < 0.01) {
                    append("""<circle cx="$cx" cy="$cy" r="$r" fill="#0A0A0B"/>""")
                } else if (!(phase > 0.49 && phase < 0.51)) {
                    if (phase < 0.5) {
                        val termR = r * Math.abs(1.0 - 4.0 * phase)
                        if (phase < 0.25) {
                            append("""<ellipse cx="$cx" cy="$cy" rx="$termR" ry="$r" fill="#0A0A0B"/>""")
                            append("""<rect x="0" y="${cy - r}" width="$cx" height="${r * 2}" fill="#0A0A0B"/>""")
                        } else {
                            append("""<rect x="0" y="${cy - r}" width="$cx" height="${r * 2}" fill="#0A0A0B"/>""")
                            if (termR >= 0.5) append("""<ellipse cx="$cx" cy="$cy" rx="$termR" ry="$r" fill="#FAD075"/>""")
                        }
                    } else {
                        val termR = r * Math.abs(1.0 - 4.0 * (phase - 0.5))
                        if (phase < 0.75) {
                            append("""<ellipse cx="$cx" cy="$cy" rx="$termR" ry="$r" fill="#0A0A0B"/>""")
                            append("""<rect x="$cx" y="${cy - r}" width="$cx" height="${r * 2}" fill="#0A0A0B"/>""")
                        } else {
                            append("""<rect x="$cx" y="${cy - r}" width="$cx" height="${r * 2}" fill="#0A0A0B"/>""")
                            if (termR >= 0.5) append("""<ellipse cx="$cx" cy="$cy" rx="$termR" ry="$r" fill="#FAD075"/>""")
                        }
                    }
                }
                append("</svg>")
            }
            val encoded = "data:image/svg+xml," + svg.replace("#", "%23").replace("\"", "'").replace("<", "%3C").replace(">", "%3E")
            appendLine("""    <div class="card" style="padding:0.5rem" title="$name">""")
            appendLine("""      <img src="$encoded" alt="$name" style="width:${size}px;height:${size}px">""")
            appendLine("    </div>")
        }
        appendLine("  </div>")
    }

    @Suppress("UNUSED_PARAMETER")
    private fun StringBuilder.appendSwatches(config: BrandConfig) {
        val swatches = listOf(
            Pair("--mystic-primary", hex(config.colors.primary)),
            Pair("--mystic-primary-dark", hex(config.colors.primaryDark)),
            Pair("--mystic-primary-light", hex(config.colors.primaryLight)),
            Pair("--mystic-bg-dark", hex(config.colors.backgroundDark)),
            Pair("--mystic-bg-surface", hex(config.colors.backgroundSurface)),
            Pair("--mystic-bg-panel", hex(config.colors.backgroundPanel)),
            Pair("--mystic-fg-primary", hex(config.colors.foregroundPrimary)),
            Pair("--mystic-fg-muted", hex(config.colors.foregroundMuted)),
            Pair("--mystic-fg-border", hex(config.colors.foregroundBorder)),
            Pair("--mystic-accent-gold", hex(config.colors.accentGold)),
            Pair("--mystic-accent-rose", hex(config.colors.accentRose)),
            Pair("--mystic-accent-seafoam", hex(config.colors.accentSeafoam)),
            Pair("--mystic-accent-lavender", hex(config.colors.accentLavender)),
            Pair("--mystic-accent-ivory", hex(config.colors.accentIvory))
        )
        for ((name, hexVal) in swatches) {
            val textColor = if (luminance(hexVal) > 0.5) "#000" else "#fff"
            appendLine("""    <div class="swatch" style="background:$hexVal;color:$textColor">$name<br>$hexVal</div>""")
        }
    }

    private fun hex(c: Color): String = "#%02X%02X%02X".format(c.red, c.green, c.blue)

    private fun luminance(hex: String): Double {
        val h = hex.removePrefix("#")
        val r = h.substring(0, 2).toInt(16) / 255.0
        val g = h.substring(2, 4).toInt(16) / 255.0
        val b = h.substring(4, 6).toInt(16) / 255.0
        return 0.299 * r + 0.587 * g + 0.114 * b
    }
}
