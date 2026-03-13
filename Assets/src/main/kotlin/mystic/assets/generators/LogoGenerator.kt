package mystic.assets.generators

import mystic.assets.config.BrandConfig
import mystic.assets.model.*
import java.awt.Color

class LogoGenerator(private val config: BrandConfig) {

    fun generate(): List<Asset> {
        val assets = mutableListOf<Asset>()

        for (variant in config.logos.variants) {
            for (themeName in config.logos.themes) {
                val theme = Theme.valueOf(themeName.uppercase())
                assets.add(generateLogo(variant, theme))
            }
        }

        for ((name, spec) in config.logos.socialSizes) {
            assets.add(generateSocialLogo(name, spec.width, spec.height))
        }

        assets.add(generateWatermark())

        for (variant in config.logos.variants) {
            for (themeName in config.logos.themes) {
                val theme = Theme.valueOf(themeName.uppercase())
                assets.add(generateLogoSvg(variant, theme))
            }
        }

        return assets
    }

    private fun generateLogo(variant: String, theme: Theme): Asset {
        val width: Int
        val height: Int

        when (variant) {
            "full" -> { width = 600; height = 200 }
            "icon-only" -> { width = 200; height = 200 }
            "wordmark" -> { width = 400; height = 100 }
            else -> { width = 600; height = 200 }
        }

        val (bg, _) = ZenMoonRenderer.colorForTheme(config.colors, theme)
        val moonColor = ZenMoonRenderer.moonColorForTheme(config.colors, config.logo, theme)
        val textColor = ZenMoonRenderer.textColorForTheme(config.colors, theme)

        val (img, g) = ZenMoonRenderer.createCanvas(width, height, bg)

        if (theme == Theme.DARK) {
            ZenMoonRenderer.drawStarField(g, width, height, config.stars, config.colors.foregroundPrimary)
        }

        when (variant) {
            "full" -> {
                val moonRadius = height * 0.32
                val moonCx = height * 0.5
                val moonCy = height * 0.5
                ZenMoonRenderer.drawMoonCrescent(
                    g, moonCx, moonCy, moonRadius, config.logo,
                    moonColor, glow = false
                )
                val fontSize = height * 0.35f
                val textY = moonCy - fontSize * 0.45
                ZenMoonRenderer.drawWordmark(
                    g, config.name,
                    moonCx + moonRadius + height * 0.15,
                    textY,
                    fontSize,
                    textColor,
                    config.typography.display,
                    config.typography.tracking
                )
            }
            "icon-only" -> {
                val moonRadius = width * 0.38
                ZenMoonRenderer.drawMoonCrescent(
                    g, width / 2.0, height / 2.0, moonRadius, config.logo,
                    moonColor, glow = false
                )
            }
            "wordmark" -> {
                ZenMoonRenderer.drawWordmark(
                    g, config.name,
                    width * 0.05,
                    height * 0.15,
                    (height * 0.55f),
                    textColor,
                    config.typography.display,
                    config.typography.tracking
                )
            }
        }

        g.dispose()

        return Asset(
            type = AssetType.LOGO,
            format = AssetFormat.PNG,
            name = "logo-${variant}-${theme.name.lowercase()}",
            width = width, height = height,
            theme = theme,
            image = img
        )
    }

    private fun generateSocialLogo(name: String, width: Int, height: Int): Asset {
        val (img, g) = ZenMoonRenderer.createCanvas(width, height, config.colors.backgroundDark)

        ZenMoonRenderer.drawStarField(g, width, height, config.stars, config.colors.foregroundPrimary)

        val moonRadius = height * 0.20
        val moonCx = width * 0.22
        val moonCy = height * 0.48

        ZenMoonRenderer.drawMoonCrescent(
            g, moonCx, moonCy, moonRadius, config.logo,
            config.logo.moonGold, glow = false
        )

        val textX = moonCx + moonRadius + width * 0.04
        val titleSize = height * 0.20f
        val taglineSize = height * 0.06f
        val titleY = height * 0.28

        ZenMoonRenderer.drawWordmark(
            g, config.name,
            textX, titleY,
            titleSize,
            config.colors.foregroundPrimary,
            config.typography.display,
            config.typography.tracking
        )

        ZenMoonRenderer.drawWordmark(
            g, config.tagline,
            textX, titleY + titleSize + height * 0.04,
            taglineSize,
            config.colors.foregroundMuted,
            config.typography.body,
            0f
        )

        g.dispose()

        return Asset(
            type = AssetType.LOGO,
            format = AssetFormat.PNG,
            name = "logo-social-$name",
            width = width, height = height,
            theme = Theme.DARK,
            image = img
        )
    }

    private fun generateWatermark(): Asset {
        val size = 200
        val (img, g) = ZenMoonRenderer.createCanvas(size, size, Color(0, 0, 0, 0))

        val gold = config.logo.moonGold
        val alpha = (config.logos.watermarkOpacity * 255).toInt()
        val moonColor = Color(gold.red, gold.green, gold.blue, alpha)

        ZenMoonRenderer.drawMoonCrescent(
            g, size / 2.0, size / 2.0, size * 0.38, config.logo,
            moonColor, glow = false, outline = false
        )

        g.dispose()

        return Asset(
            type = AssetType.LOGO,
            format = AssetFormat.PNG,
            name = "logo-watermark",
            width = size, height = size,
            theme = Theme.DARK,
            image = img
        )
    }

    private fun generateLogoSvg(variant: String, theme: Theme): Asset {
        val (bgHex, moonHex, textHex) = svgColorsForTheme(theme)

        val svg = when (variant) {
            "full" -> buildFullLogoSvg(600, 200, bgHex, moonHex, textHex)
            "icon-only" -> ZenMoonRenderer.generateMoonSvg(200, config.logo, moonHex, bgHex)
            "wordmark" -> buildWordmarkSvg(400, 100, bgHex, textHex)
            else -> ""
        }

        return Asset(
            type = AssetType.LOGO,
            format = AssetFormat.SVG,
            name = "logo-${variant}-${theme.name.lowercase()}",
            width = if (variant == "icon-only") 200 else if (variant == "wordmark") 400 else 600,
            height = if (variant == "wordmark") 100 else 200,
            theme = theme,
            svgContent = svg
        )
    }

    private fun buildFullLogoSvg(
        width: Int, height: Int,
        bgHex: String, moonHex: String, textHex: String
    ): String {
        val moonR = height * 0.32
        val moonCx = height * 0.5
        val moonCy = height * 0.5
        val innerR = moonR * config.logo.moonCrescentRatio
        val innerCx = moonCx + moonR * config.logo.moonOffsetX
        val innerCy = moonCy + moonR * config.logo.moonOffsetY
        val textX = moonCx + moonR + height * 0.15
        val textY = height * 0.58

        return buildString {
            appendLine("""<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height" viewBox="0 0 $width $height">""")
            appendLine("""  <rect width="$width" height="$height" fill="$bgHex"/>""")
            appendLine("  <defs>")
            appendLine("""    <mask id="crescent-full">""")
            appendLine("""      <rect width="$width" height="$height" fill="black"/>""")
            appendLine("""      <circle cx="$moonCx" cy="$moonCy" r="$moonR" fill="white"/>""")
            appendLine("""      <circle cx="$innerCx" cy="$innerCy" r="$innerR" fill="black"/>""")
            appendLine("    </mask>")
            appendLine("  </defs>")
            appendLine("""  <rect width="$width" height="$height" fill="$moonHex" mask="url(#crescent-full)"/>""")
            appendLine("""  <text x="$textX" y="$textY" font-family="Inter, system-ui, sans-serif" font-size="${height * 0.35}" font-weight="500" fill="$textHex" letter-spacing="${config.typography.tracking}em">${config.name}</text>""")
            appendLine("</svg>")
        }
    }

    private fun buildWordmarkSvg(
        width: Int, height: Int,
        bgHex: String, textHex: String
    ): String = buildString {
        appendLine("""<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height" viewBox="0 0 $width $height">""")
        appendLine("""  <rect width="$width" height="$height" fill="$bgHex"/>""")
        appendLine("""  <text x="${width * 0.05}" y="${height * 0.68}" font-family="Inter, system-ui, sans-serif" font-size="${height * 0.55}" font-weight="500" fill="$textHex" letter-spacing="${config.typography.tracking}em">${config.name}</text>""")
        appendLine("</svg>")
    }

    private fun svgColorsForTheme(theme: Theme): Triple<String, String, String> = when (theme) {
        Theme.DARK -> Triple("#0A0A0B", "#FAD075", "#FAFAFA")
        Theme.LIGHT -> Triple("#FFFFFF", "#C9A84C", "#09090B")
        Theme.MONO -> Triple("#0A0A0B", "#FAFAFA", "#FAFAFA")
    }
}
