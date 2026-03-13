package dotai.assets.generators

import dotai.assets.config.BrandConfig
import dotai.assets.model.*

class IconGenerator(private val config: BrandConfig) {

    fun generate(): List<Asset> {
        val assets = mutableListOf<Asset>()

        for (size in config.icons.desktopSizes) {
            for (theme in Theme.entries) {
                assets.add(generateIcon(size, theme))
            }
        }

        for (size in config.icons.faviconSizes) {
            assets.add(generateIcon(size, Theme.DARK, prefix = "favicon"))
        }

        for (theme in Theme.entries) {
            val svg = generateIconSvg(512, theme)
            assets.add(Asset(
                type = AssetType.ICON,
                format = AssetFormat.SVG,
                name = "icon-${theme.name.lowercase()}",
                width = 512, height = 512,
                theme = theme,
                svgContent = svg
            ))
        }

        return assets
    }

    private fun generateIcon(size: Int, theme: Theme, prefix: String = "icon"): Asset {
        val (bg, _) = ZenMoonRenderer.colorForTheme(config.colors, theme)
        val moonColor = ZenMoonRenderer.moonColorForTheme(config.colors, theme)

        val (img, g) = ZenMoonRenderer.createCanvas(size, size, bg)

        val cx = size / 2.0
        val cy = size / 2.0
        val radius = size * 0.38

        val showGlow = size >= 64 && theme == Theme.DARK
        ZenMoonRenderer.drawMoonCrescent(
            g, cx, cy, radius, config.logo,
            moonColor, glow = showGlow, glowColor = config.colors.primary
        )

        g.dispose()

        return Asset(
            type = AssetType.ICON,
            format = AssetFormat.PNG,
            name = "${prefix}-${theme.name.lowercase()}-${size}x${size}",
            width = size, height = size,
            theme = theme,
            image = img
        )
    }

    private fun generateIconSvg(size: Int, theme: Theme): String {
        val (_, moonHex) = svgColorsForTheme(theme)
        val bgHex = svgBgForTheme(theme)
        return ZenMoonRenderer.generateMoonSvg(size, config.logo, moonHex, bgHex)
    }

    private fun svgColorsForTheme(theme: Theme): Pair<String, String> = when (theme) {
        Theme.DARK -> Pair("#000000", "#0EA5E9")
        Theme.LIGHT -> Pair("#FFFFFF", "#0284C7")
        Theme.MONO -> Pair("#000000", "#E5E5E5")
    }

    private fun svgBgForTheme(theme: Theme): String = when (theme) {
        Theme.DARK -> "#000000"
        Theme.LIGHT -> "#FFFFFF"
        Theme.MONO -> "#000000"
    }
}
