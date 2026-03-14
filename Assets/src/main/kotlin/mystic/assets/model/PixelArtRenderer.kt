package mystic.assets.model

import mystic.assets.config.BrandConfig
import mystic.assets.config.ColorConfig
import mystic.assets.config.LogoConfig
import mystic.assets.config.StarConfig
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Renders pixel-art style assets by drawing at high resolution and downscaling
 * with nearest-neighbor to preserve crisp pixel edges.
 */
object PixelArtRenderer {

    private const val SCALE_FACTOR = 4

    /**
     * Renders an icon at high resolution, then downscales with nearest-neighbor
     * to produce a pixel-art effect.
     */
    fun renderPixelIcon(
        size: Int,
        theme: Theme,
        config: BrandConfig
    ): BufferedImage {
        val sourceSize = size * SCALE_FACTOR
        val (bg, _) = ZenMoonRenderer.colorForTheme(config.colors, theme)
        val moonColor = ZenMoonRenderer.moonColorForTheme(config.colors, config.logo, theme)

        val (sourceImg, g) = ZenMoonRenderer.createCanvas(sourceSize, sourceSize, bg)

        if (theme == Theme.DARK && sourceSize >= 32) {
            ZenMoonRenderer.drawStarField(g, sourceSize, sourceSize, config.stars, config.colors.foregroundPrimary)
        }

        val cx = sourceSize / 2.0
        val cy = sourceSize / 2.0
        val radius = sourceSize * 0.38

        ZenMoonRenderer.drawMoonCrescent(
            g, cx, cy, radius, config.logo,
            moonColor, glow = false
        )

        g.dispose()

        return downscaleNearest(sourceImg, size, size)
    }

    /**
     * Downscales an image using nearest-neighbor interpolation for crisp pixel edges.
     */
    fun downscaleNearest(
        source: BufferedImage,
        targetWidth: Int,
        targetHeight: Int
    ): BufferedImage {
        val scaled = source.getScaledInstance(
            targetWidth, targetHeight,
            Image.SCALE_REPLICATE
        )

        val result = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB)
        val g = result.createGraphics()
        g.drawImage(scaled, 0, 0, null)
        g.dispose()

        return result
    }
}
