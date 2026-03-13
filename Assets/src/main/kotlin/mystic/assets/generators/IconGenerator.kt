package mystic.assets.generators

import mystic.assets.config.BrandConfig
import mystic.assets.model.*

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
        val moonColor = ZenMoonRenderer.moonColorForTheme(config.colors, config.logo, theme)

        val (img, g) = ZenMoonRenderer.createCanvas(size, size, bg)

        if (theme == Theme.DARK && size >= 32) {
            ZenMoonRenderer.drawStarField(g, size, size, config.stars, config.colors.foregroundPrimary)
        }

        val cx = size / 2.0
        val cy = size / 2.0
        val radius = size * 0.38

        ZenMoonRenderer.drawMoonCrescent(
            g, cx, cy, radius, config.logo,
            moonColor, glow = false
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
        Theme.DARK -> Pair("#0A0A0B", "#FAD075")
        Theme.LIGHT -> Pair("#FFFFFF", "#C9A84C")
        Theme.MONO -> Pair("#0A0A0B", "#FAFAFA")
    }

    private fun svgBgForTheme(theme: Theme): String = when (theme) {
        Theme.DARK -> "#0A0A0B"
        Theme.LIGHT -> "#FFFFFF"
        Theme.MONO -> "#0A0A0B"
    }
}
