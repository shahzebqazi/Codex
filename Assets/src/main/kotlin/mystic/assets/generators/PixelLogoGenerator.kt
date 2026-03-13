package mystic.assets.generators

import mystic.assets.config.BrandConfig
import mystic.assets.model.Asset
import mystic.assets.model.AssetFormat
import mystic.assets.model.AssetType
import mystic.assets.model.Theme
import mystic.assets.model.PixelArtRenderer
import mystic.assets.model.ZenMoonRenderer
import java.awt.image.BufferedImage

class PixelLogoGenerator(private val config: BrandConfig) {

    fun generate(): List<Asset> {
        val assets = mutableListOf<Asset>()

        for (size in config.pixelArt.sizes) {
            for (theme in Theme.entries) {
                assets.add(generatePixelIconOnly(size, theme))
            }
        }

        for (theme in Theme.entries) {
            assets.add(generatePixelWordmark(theme))
        }

        return assets
    }

    private fun generatePixelIconOnly(size: Int, theme: Theme): Asset {
        val img = PixelArtRenderer.renderPixelIcon(size, theme, config)
        return Asset(
            type = AssetType.PIXEL_LOGO,
            format = AssetFormat.PNG,
            name = "logo-icon-only-${theme.name.lowercase()}-${size}x${size}",
            width = size,
            height = size,
            theme = theme,
            image = img
        )
    }

    private fun generatePixelWordmark(theme: Theme): Asset {
        val width = 160
        val height = 40
        val sourceWidth = width * 4
        val sourceHeight = height * 4

        val (bg, _) = ZenMoonRenderer.colorForTheme(config.colors, theme)
        val textColor = ZenMoonRenderer.textColorForTheme(config.colors, theme)

        val (sourceImg, g) = ZenMoonRenderer.createCanvas(sourceWidth, sourceHeight, bg)
        ZenMoonRenderer.drawWordmark(
            g, config.name,
            sourceWidth * 0.05,
            sourceHeight * 0.15,
            (sourceHeight * 0.55f),
            textColor,
            config.typography.display,
            config.typography.tracking
        )
        g.dispose()

        val img = PixelArtRenderer.downscaleNearest(sourceImg, width, height)

        return Asset(
            type = AssetType.PIXEL_LOGO,
            format = AssetFormat.PNG,
            name = "logo-wordmark-${theme.name.lowercase()}",
            width = width,
            height = height,
            theme = theme,
            image = img
        )
    }
}
