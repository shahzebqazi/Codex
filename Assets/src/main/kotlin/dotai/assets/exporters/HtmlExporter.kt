package dotai.assets.exporters

import dotai.assets.config.BrandConfig
import dotai.assets.model.Asset
import dotai.assets.model.AssetFormat
import java.io.File

class HtmlExporter(private val outputDir: File) {

    fun export(assets: List<Asset>, config: BrandConfig): File {
        val dir = File(outputDir, "preview")
        dir.mkdirs()
        val file = File(dir, "index.html")

        val pngAssets = assets.filter { it.format == AssetFormat.PNG && it.image != null }
        val svgAssets = assets.filter { it.format == AssetFormat.SVG && it.svgContent != null }

        val html = buildString {
            appendLine("<!DOCTYPE html>")
            appendLine("""<html lang="en">""")
            appendLine("<head>")
            appendLine("""  <meta charset="UTF-8">""")
            appendLine("""  <meta name="viewport" content="width=device-width, initial-scale=1.0">""")
            appendLine("  <title>dotAi Brand Assets Preview</title>")
            appendLine("""  <link rel="stylesheet" href="../css/brand.css">""")
            appendLine("  <style>")
            appendLine("    * { margin: 0; padding: 0; box-sizing: border-box; }")
            appendLine("    body {")
            appendLine("      background: var(--dotai-bg-dark);")
            appendLine("      color: var(--dotai-fg-primary);")
            appendLine("      font-family: var(--dotai-font-body);")
            appendLine("      padding: 2rem;")
            appendLine("    }")
            appendLine("    h1 { font-size: 2rem; margin-bottom: 0.5rem; letter-spacing: var(--dotai-tracking); }")
            appendLine("    h2 { font-size: 1.25rem; color: var(--dotai-fg-muted); margin: 2rem 0 1rem; border-bottom: 1px solid var(--dotai-fg-border); padding-bottom: 0.5rem; }")
            appendLine("    .subtitle { color: var(--dotai-fg-muted); margin-bottom: 2rem; }")
            appendLine("    .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 1.5rem; }")
            appendLine("    .card {")
            appendLine("      background: var(--dotai-bg-surface);")
            appendLine("      border: 1px solid var(--dotai-fg-border);")
            appendLine("      border-radius: 8px;")
            appendLine("      padding: 1rem;")
            appendLine("      display: flex;")
            appendLine("      flex-direction: column;")
            appendLine("      align-items: center;")
            appendLine("    }")
            appendLine("    .card img { max-width: 100%; height: auto; margin-bottom: 0.75rem; }")
            appendLine("    .card .label { font-size: 0.75rem; color: var(--dotai-fg-muted); font-family: var(--dotai-font-mono); word-break: break-all; text-align: center; }")
            appendLine("    .card .dims { font-size: 0.65rem; color: var(--dotai-fg-border); margin-top: 0.25rem; }")
            appendLine("    .wide-grid { grid-template-columns: 1fr; }")
            appendLine("    .wide-grid .card img { max-height: 300px; object-fit: contain; }")
            appendLine("    .checkerboard { background: repeating-conic-gradient(#1a1a1a 0% 25%, #0a0a0a 0% 50%) 0 0 / 20px 20px; padding: 1rem; border-radius: 4px; }")
            appendLine("    .swatch-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 0.75rem; }")
            appendLine("    .swatch { height: 60px; border-radius: 6px; display: flex; align-items: flex-end; padding: 0.35rem 0.5rem; font-size: 0.65rem; font-family: var(--dotai-font-mono); }")
            appendLine("  </style>")
            appendLine("</head>")
            appendLine("<body>")
            appendLine("  <h1>dotAi Brand Assets</h1>")
            appendLine("""  <p class="subtitle">Generated asset preview &mdash; ${assets.size} assets total</p>""")

            appendLine("  <h2>Color Palette</h2>")
            appendLine("""  <div class="swatch-grid">""")
            appendSwatches(config)
            appendLine("  </div>")

            for (section in listOf("icons", "logos", "banners", "heroes")) {
                val sectionAssets = pngAssets.filter { it.subdirectory() == section }
                if (sectionAssets.isEmpty()) continue

                appendLine("  <h2>${section.replaceFirstChar { it.uppercase() }}</h2>")
                val gridClass = if (section in listOf("banners", "heroes")) "grid wide-grid" else "grid"
                appendLine("""  <div class="$gridClass">""")
                for (asset in sectionAssets) {
                    val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                    appendLine("""    <div class="card">""")
                    if (asset.name.contains("watermark")) {
                        appendLine("""      <div class="checkerboard"><img src="$relPath" alt="${asset.name}"></div>""")
                    } else {
                        appendLine("""      <img src="$relPath" alt="${asset.name}">""")
                    }
                    appendLine("""      <span class="label">${asset.filename()}</span>""")
                    appendLine("""      <span class="dims">${asset.width}x${asset.height}</span>""")
                    appendLine("    </div>")
                }
                appendLine("  </div>")
            }

            if (svgAssets.isNotEmpty()) {
                appendLine("  <h2>SVG Assets</h2>")
                appendLine("""  <div class="grid">""")
                for (asset in svgAssets) {
                    val relPath = "../${asset.subdirectory()}/${asset.filename()}"
                    appendLine("""    <div class="card">""")
                    appendLine("""      <img src="$relPath" alt="${asset.name}">""")
                    appendLine("""      <span class="label">${asset.filename()}</span>""")
                    appendLine("    </div>")
                }
                appendLine("  </div>")
            }

            appendLine("</body>")
            appendLine("</html>")
        }

        file.writeText(html)
        return file
    }

    private fun StringBuilder.appendSwatches(config: BrandConfig) {
        val swatches = listOf(
            Pair("--dotai-primary", "#0EA5E9"),
            Pair("--dotai-primary-dark", "#0284C7"),
            Pair("--dotai-primary-light", "#38BDF8"),
            Pair("--dotai-bg-dark", "#000000"),
            Pair("--dotai-bg-surface", "#0A0A0A"),
            Pair("--dotai-bg-panel", "#1A1A1A"),
            Pair("--dotai-fg-primary", "#E5E5E5"),
            Pair("--dotai-fg-muted", "#666666"),
            Pair("--dotai-fg-border", "#333333"),
            Pair("--dotai-accent-emerald", "#10B981"),
            Pair("--dotai-accent-amber", "#F59E0B"),
            Pair("--dotai-accent-red", "#FF5F57")
        )
        for ((name, hex) in swatches) {
            val textColor = if (luminance(hex) > 0.5) "#000" else "#fff"
            appendLine("""    <div class="swatch" style="background:$hex;color:$textColor">$name<br>$hex</div>""")
        }
    }

    private fun luminance(hex: String): Double {
        val h = hex.removePrefix("#")
        val r = h.substring(0, 2).toInt(16) / 255.0
        val g = h.substring(2, 4).toInt(16) / 255.0
        val b = h.substring(4, 6).toInt(16) / 255.0
        return 0.299 * r + 0.587 * g + 0.114 * b
    }
}
