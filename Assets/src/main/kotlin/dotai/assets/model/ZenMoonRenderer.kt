package dotai.assets.model

import dotai.assets.config.BrandConfig
import dotai.assets.config.ColorConfig
import dotai.assets.config.LogoConfig
import java.awt.*
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

object ZenMoonRenderer {

    fun drawMoonCrescent(
        g: Graphics2D,
        cx: Double,
        cy: Double,
        radius: Double,
        config: LogoConfig,
        color: Color,
        glow: Boolean = true,
        glowColor: Color? = null
    ) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        val outerCircle = Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2)

        val innerRadius = radius * config.moonCrescentRatio
        val innerCx = cx + radius * config.moonOffsetX
        val innerCy = cy + radius * config.moonOffsetY

        val innerCircle = Ellipse2D.Double(
            innerCx - innerRadius, innerCy - innerRadius,
            innerRadius * 2, innerRadius * 2
        )

        val crescent = Area(outerCircle)
        crescent.subtract(Area(innerCircle))

        if (glow && glowColor != null) {
            val glowRad = radius * (1.0 + config.glowRadius)
            val glowPaint = RadialGradientPaint(
                cx.toFloat(), cy.toFloat(), glowRad.toFloat(),
                floatArrayOf(0.0f, 0.6f, 1.0f),
                arrayOf(
                    Color(glowColor.red, glowColor.green, glowColor.blue, (config.glowOpacity * 255).toInt()),
                    Color(glowColor.red, glowColor.green, glowColor.blue, (config.glowOpacity * 128).toInt()),
                    Color(glowColor.red, glowColor.green, glowColor.blue, 0)
                )
            )
            g.paint = glowPaint
            g.fill(Ellipse2D.Double(cx - glowRad, cy - glowRad, glowRad * 2, glowRad * 2))
        }

        g.color = color
        g.fill(crescent)
    }

    fun drawZenRipples(
        g: Graphics2D,
        cx: Double,
        cy: Double,
        count: Int,
        spacing: Int,
        strokeWidth: Float,
        color: Color,
        opacity: Float
    ) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.stroke = BasicStroke(strokeWidth)

        for (i in 1..count) {
            val r = spacing.toDouble() * i
            val alpha = ((opacity * 255) * (1.0 - (i.toDouble() / (count + 1)))).toInt().coerceIn(0, 255)
            g.color = Color(color.red, color.green, color.blue, alpha)
            g.draw(Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2))
        }
    }

    fun drawWordmark(
        g: Graphics2D,
        text: String,
        x: Double,
        y: Double,
        fontSize: Float,
        color: Color,
        fontFamily: String = "Inter",
        tracking: Float = 0.03f
    ) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)

        val baseFont = Font(fontFamily, Font.PLAIN, fontSize.toInt())
        val tracked = baseFont.deriveFont(
            mapOf(java.awt.font.TextAttribute.TRACKING to tracking)
        )
        g.font = tracked
        g.color = color

        val fm = g.fontMetrics
        g.drawString(text, x.toFloat(), (y + fm.ascent).toFloat())
    }

    fun createCanvas(width: Int, height: Int, background: Color): Pair<BufferedImage, Graphics2D> {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = img.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.color = background
        g.fillRect(0, 0, width, height)
        return Pair(img, g)
    }

    fun colorForTheme(colors: ColorConfig, theme: Theme): Pair<Color, Color> = when (theme) {
        Theme.DARK -> Pair(colors.backgroundDark, colors.primary)
        Theme.LIGHT -> Pair(colors.lightBackground, colors.lightForeground)
        Theme.MONO -> Pair(colors.backgroundDark, colors.foregroundPrimary)
    }

    fun textColorForTheme(colors: ColorConfig, theme: Theme): Color = when (theme) {
        Theme.DARK -> colors.foregroundPrimary
        Theme.LIGHT -> colors.lightForeground
        Theme.MONO -> colors.foregroundPrimary
    }

    fun moonColorForTheme(colors: ColorConfig, theme: Theme): Color = when (theme) {
        Theme.DARK -> colors.primary
        Theme.LIGHT -> colors.primaryDark
        Theme.MONO -> colors.foregroundPrimary
    }

    fun generateMoonSvg(
        size: Int,
        config: LogoConfig,
        moonColor: String,
        bgColor: String? = null
    ): String {
        val r = size * 0.38
        val cx = size / 2.0
        val cy = size / 2.0
        val innerR = r * config.moonCrescentRatio
        val innerCx = cx + r * config.moonOffsetX
        val innerCy = cy + r * config.moonOffsetY

        return buildString {
            appendLine("""<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 $size $size">""")
            if (bgColor != null) {
                appendLine("""  <rect width="$size" height="$size" fill="$bgColor"/>""")
            }
            appendLine("  <defs>")
            appendLine("""    <mask id="crescent">""")
            appendLine("""      <rect width="$size" height="$size" fill="black"/>""")
            appendLine("""      <circle cx="$cx" cy="$cy" r="$r" fill="white"/>""")
            appendLine("""      <circle cx="$innerCx" cy="$innerCy" r="$innerR" fill="black"/>""")
            appendLine("    </mask>")
            appendLine("  </defs>")
            appendLine("""  <circle cx="$cx" cy="$cy" r="${r * 1.2}" fill="$moonColor" opacity="0.15"/>""")
            appendLine("""  <rect width="$size" height="$size" fill="$moonColor" mask="url(#crescent)"/>""")
            appendLine("</svg>")
        }
    }
}
