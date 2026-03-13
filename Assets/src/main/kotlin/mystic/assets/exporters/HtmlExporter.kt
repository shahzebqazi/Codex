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
        val brandGuide  = svg("""<path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>""")
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
            appendLine("""  <link rel="stylesheet" href="../css/brand.css">""")
            appendStyles()
            appendLine("</head>")
            appendLine("<body>")
            appendLine("""<nav class="top-nav">""")
            appendLine("""  <div class="top-nav-inner">""")
            appendLine("""    <div class="top-nav-spacer"></div>""")
            appendLine("""    <div class="top-nav-links">""")
            appendLine("""      <a href="#section-overview">Overview</a>""")

            appendLine("""      <a href="#" class="top-nav-active">Brand Guide</a>""")
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
                appendLine("""    <h3>Research</h3>""")
                appendLine("""    <ul>""")
                appendLine("""      <li><a href="#section-moon-phases">${Icons.moonPhases} Moon Phases</a></li>""")
                appendLine("""      <li><a href="#section-twinkling-stars">${Icons.stars} Twinkling Stars</a></li>""")
                appendLine("""    </ul>""")
                appendLine("""  </nav>""")
            }

            // ── Research ──
            appendLine("""  <div class="group-heading">${Icons.research} Research</div>""")

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
        appendLine("    .top-nav-inner { max-width: 1200px; margin: 0 auto; padding: 0 1rem; display: flex; align-items: center; justify-content: space-between; height: 2.75rem; }")
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

        // ── Star Animations ──
        appendLine("    .star-field { position: relative; overflow: hidden; }")
        appendLine("    .star { position: absolute; background: var(--mystic-accent-gold); animation-name: twinkle; animation-iteration-count: infinite; animation-timing-function: ease-in-out; border-radius: 50%; }")
        appendLine("    .star-bright { position: absolute; animation: twinkle 2s ease-in-out infinite; }")

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
        appendLine("""  <div class="type-row">""")
        for ((size, label) in listOf(48 to "Display / 48px", 32 to "Display / 32px", 24 to "Display / 24px")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-display);font-size:${size}px;letter-spacing:var(--mystic-tracking);line-height:1.2">${config.name}</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.display} &middot; $label</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")

        appendLine("""  <div class="type-row">""")
        for ((size, label) in listOf(16 to "Body / 16px", 14 to "Body / 14px", 12 to "Body / 12px")) {
            appendLine("""    <div class="type-specimen">""")
            appendLine("""      <div style="font-family:var(--mystic-font-body);font-size:${size}px;line-height:1.5">${config.tagline}</div>""")
            appendLine("""      <div class="specimen-label">${config.typography.body} &middot; $label</div>""")
            appendLine("    </div>")
        }
        appendLine("  </div>")

        appendLine("""  <div class="type-specimen" style="max-width:600px">""")
        appendLine("""    <div style="font-family:var(--mystic-font-mono);font-size:14px;line-height:1.6;color:var(--mystic-accent-seafoam)">val greeting = "Hello, ${config.name}"</div>""")
        appendLine("""    <div class="specimen-label">${config.typography.mono} &middot; Mono / 14px &middot; tracking: ${config.typography.tracking}em</div>""")
        appendLine("  </div>")

        appendLine("""  <div class="type-row" style="margin-top:1rem">""")
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
            Triple("--mystic-font-body", "'${config.typography.body}'", "Body text and paragraphs"),
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
