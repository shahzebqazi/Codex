package dotai.assets.generators

import dotai.assets.config.BrandConfig
import dotai.assets.model.*
import java.awt.GradientPaint

class BannerGenerator(private val config: BrandConfig) {

    fun generate(): List<Asset> {
        return config.banners.map { (name, spec) ->
            generateBanner(name, spec.width, spec.height)
        }
    }

    private fun generateBanner(name: String, width: Int, height: Int): Asset {
        val (img, g) = ZenMoonRenderer.createCanvas(width, height, config.colors.backgroundDark)

        val gradient = GradientPaint(
            0f, 0f, config.colors.backgroundDark,
            width.toFloat(), height.toFloat(), config.colors.backgroundSurface
        )
        g.paint = gradient
        g.fillRect(0, 0, width, height)

        val moonRadius = height * 0.22
        val moonCx = width * 0.75
        val moonCy = height * 0.4

        ZenMoonRenderer.drawMoonCrescent(
            g, moonCx, moonCy, moonRadius, config.logo,
            config.colors.primary, glow = true, glowColor = config.colors.primary
        )

        ZenMoonRenderer.drawZenRipples(
            g, moonCx, moonCy + moonRadius * 1.8,
            count = 3, spacing = (height * 0.08).toInt(),
            strokeWidth = 1.0f, color = config.colors.foregroundBorder,
            opacity = 0.06f
        )

        val titleSize = when (name) {
            "readme" -> height * 0.28f
            else -> height * 0.14f
        }
        val taglineSize = titleSize * 0.4f

        ZenMoonRenderer.drawWordmark(
            g, config.name,
            width * 0.06, height * 0.25,
            titleSize, config.colors.foregroundPrimary,
            config.typography.display, config.typography.tracking
        )

        ZenMoonRenderer.drawWordmark(
            g, config.tagline,
            width * 0.06, height * 0.25 + titleSize * 1.3,
            taglineSize, config.colors.foregroundMuted,
            config.typography.body, 0f
        )

        g.dispose()

        return Asset(
            type = AssetType.BANNER,
            format = AssetFormat.PNG,
            name = "banner-$name",
            width = width, height = height,
            theme = Theme.DARK,
            image = img
        )
    }
}
