package mystic.assets.generators

import mystic.assets.config.BrandConfig
import mystic.assets.model.*
import java.awt.GradientPaint
class HeroGenerator(private val config: BrandConfig) {

    fun generate(): List<Asset> = listOf(
        generateHero("hero-landing", config.heroes.landing.width, config.heroes.landing.height),
        generateHero("hero-landing-retina", config.heroes.landingRetina.width, config.heroes.landingRetina.height)
    )

    private fun generateHero(name: String, width: Int, height: Int): Asset {
        val (img, g) = ZenMoonRenderer.createCanvas(width, height, config.colors.backgroundDark)

        val gradient = GradientPaint(
            0f, 0f, config.colors.backgroundDark,
            0f, height.toFloat(), config.colors.backgroundSurface
        )
        g.paint = gradient
        g.fillRect(0, 0, width, height)

        ZenMoonRenderer.drawStarField(g, width, height, config.stars, config.colors.foregroundPrimary)

        val moonRadius = height * 0.18
        val moonCx = width * 0.5
        val moonCy = height * 0.35

        ZenMoonRenderer.drawMoonCrescent(
            g, moonCx, moonCy, moonRadius, config.logo,
            config.logo.moonGold, glow = false
        )

        val titleSize = height * 0.06f
        val titleY = moonCy + moonRadius * 2.2 + height * 0.06

        val titleFont = java.awt.Font(config.typography.display, java.awt.Font.PLAIN, titleSize.toInt())
        g.font = titleFont
        val titleWidth = g.fontMetrics.stringWidth(config.name)
        val titleX = (width - titleWidth) / 2.0

        ZenMoonRenderer.drawWordmark(
            g, config.name,
            titleX, titleY,
            titleSize, config.colors.foregroundPrimary,
            config.typography.display, config.typography.tracking
        )

        val tagSize = height * 0.025f
        val tagFont = java.awt.Font(config.typography.body, java.awt.Font.PLAIN, tagSize.toInt())
        g.font = tagFont
        val tagWidth = g.fontMetrics.stringWidth(config.tagline)
        val tagX = (width - tagWidth) / 2.0

        ZenMoonRenderer.drawWordmark(
            g, config.tagline,
            tagX, titleY + titleSize + height * 0.025,
            tagSize, config.colors.foregroundMuted,
            config.typography.body, 0f
        )

        g.dispose()

        return Asset(
            type = AssetType.HERO,
            format = AssetFormat.PNG,
            name = name,
            width = width, height = height,
            theme = Theme.DARK,
            image = img
        )
    }
}
