package dotai.assets.generators

import dotai.assets.config.BrandConfig
import dotai.assets.model.*
import java.awt.Color
import java.awt.GradientPaint
import java.awt.RadialGradientPaint

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

        val moonRadius = height * 0.18
        val moonCx = width * 0.5
        val moonCy = height * 0.35

        val ambientGlow = RadialGradientPaint(
            moonCx.toFloat(), moonCy.toFloat(), (moonRadius * 4).toFloat(),
            floatArrayOf(0f, 0.4f, 1f),
            arrayOf(
                Color(config.colors.primary.red, config.colors.primary.green, config.colors.primary.blue, 20),
                Color(config.colors.primary.red, config.colors.primary.green, config.colors.primary.blue, 8),
                Color(0, 0, 0, 0)
            )
        )
        g.paint = ambientGlow
        g.fillRect(0, 0, width, height)

        ZenMoonRenderer.drawMoonCrescent(
            g, moonCx, moonCy, moonRadius, config.logo,
            config.colors.primary, glow = true, glowColor = config.colors.primary
        )

        val rippleCenterY = moonCy + moonRadius * 2.5
        ZenMoonRenderer.drawZenRipples(
            g, moonCx, rippleCenterY,
            count = config.heroes.rippleCount,
            spacing = config.heroes.rippleSpacing,
            strokeWidth = config.heroes.rippleStrokeWidth,
            color = config.colors.foregroundBorder,
            opacity = config.heroes.rippleOpacity
        )

        val titleSize = height * 0.06f
        ZenMoonRenderer.drawWordmark(
            g, config.name,
            width * 0.5 - titleSize * 1.5,
            rippleCenterY + config.heroes.rippleCount * config.heroes.rippleSpacing + height * 0.04,
            titleSize, config.colors.foregroundPrimary,
            config.typography.display, config.typography.tracking
        )

        val tagSize = height * 0.025f
        ZenMoonRenderer.drawWordmark(
            g, config.tagline,
            width * 0.5 - tagSize * config.tagline.length * 0.25,
            rippleCenterY + config.heroes.rippleCount * config.heroes.rippleSpacing + height * 0.04 + titleSize * 1.6,
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
