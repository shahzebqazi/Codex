package mystic.assets.model

import mystic.assets.config.ColorConfig
import mystic.assets.config.LogoConfig
import mystic.assets.config.StarConfig
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

object ZenMoonRenderer {

    fun drawMoonCrescent(
        g: Graphics2D,
        cx: Double,
        cy: Double,
        radius: Double,
        config: LogoConfig,
        color: Color,
        @Suppress("UNUSED_PARAMETER") glow: Boolean = false,
        outline: Boolean = true
    ) {
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

        val innerRadius = radius * config.moonCrescentRatio
        val innerCx = cx + radius * config.moonOffsetX
        val innerCy = cy + radius * config.moonOffsetY

        val outerCircle = Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2)
        val innerCircle = Ellipse2D.Double(
            innerCx - innerRadius, innerCy - innerRadius,
            innerRadius * 2, innerRadius * 2
        )

        val crescent = Area(outerCircle)
        crescent.subtract(Area(innerCircle))

        val rotationRad = Math.toRadians(config.moonRotation)
        val rotatedCrescent = crescent.createTransformedArea(
            AffineTransform.getRotateInstance(rotationRad, cx, cy)
        )

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)

        val pixelSize = max(1, (radius / 16).toInt())
        val bounds = rotatedCrescent.bounds
        val gridMinX = bounds.x / pixelSize
        val gridMaxX = (bounds.x + bounds.width) / pixelSize + 1
        val gridMinY = bounds.y / pixelSize
        val gridMaxY = (bounds.y + bounds.height) / pixelSize + 1

        val darkShade = Color(max(0, color.red - 50), max(0, color.green - 40), max(0, color.blue - 40))
        val highlight = Color(min(255, color.red + 20), min(255, color.green + 20), min(255, color.blue + 30))

        val crescentPixels = mutableSetOf<Long>()

        for (gy in gridMinY..gridMaxY) {
            for (gx in gridMinX..gridMaxX) {
                val centerX = gx * pixelSize + pixelSize / 2.0
                val centerY = gy * pixelSize + pixelSize / 2.0
                if (rotatedCrescent.contains(centerX, centerY)) {
                    crescentPixels.add(gx.toLong() shl 32 or (gy.toLong() and 0xFFFFFFFFL))

                    val dx = centerX - innerCx
                    val dy = centerY - innerCy
                    val distFromInnerCenter = sqrt(dx * dx + dy * dy)
                    val distFromInnerEdge = (distFromInnerCenter - innerRadius).coerceAtLeast(0.0)
                    val maxDist = radius - innerRadius * 0.5
                    val t = (distFromInnerEdge / maxDist).coerceIn(0.0, 1.0)

                    val pixelColor = when {
                        t < 0.3 -> darkShade
                        t < 0.65 -> color
                        else -> highlight
                    }

                    g.color = pixelColor
                    g.fillRect(gx * pixelSize, gy * pixelSize, pixelSize, pixelSize)
                }
            }
        }

        if (outline) {
            g.color = Color.BLACK
            for (key in crescentPixels) {
                val gx = (key shr 32).toInt()
                val gy = (key and 0xFFFFFFFFL).toInt()
                val neighbors = arrayOf(
                    (gx - 1).toLong() shl 32 or (gy.toLong() and 0xFFFFFFFFL),
                    (gx + 1).toLong() shl 32 or (gy.toLong() and 0xFFFFFFFFL),
                    gx.toLong() shl 32 or ((gy - 1).toLong() and 0xFFFFFFFFL),
                    gx.toLong() shl 32 or ((gy + 1).toLong() and 0xFFFFFFFFL)
                )
                for (nKey in neighbors) {
                    if (nKey !in crescentPixels) {
                        val nx = (nKey shr 32).toInt()
                        val ny = (nKey and 0xFFFFFFFFL).toInt()
                        val px = nx * pixelSize
                        val py = ny * pixelSize
                        if (px >= 0 && py >= 0 && px < g.deviceConfiguration.bounds.width &&
                            py < g.deviceConfiguration.bounds.height) {
                            g.fillRect(px, py, pixelSize, pixelSize)
                        }
                    }
                }
            }
        }
    }

    private data class StarPosition(val nx: Double, val ny: Double, val size: Float, val alpha: Float, val colorIndex: Int)
    private data class SparklePosition(val nx: Double, val ny: Double, val colorIndex: Int, val rayLength: Int)

    private val starCache = mutableMapOf<Long, Pair<List<StarPosition>, List<SparklePosition>>>()

    private val STAR_PALETTE = arrayOf(
        Color(0xFA, 0xD0, 0x75),
        Color(0xFD, 0xE8, 0xB0),
        Color(0xFF, 0xFF, 0xFF),
        Color(0xCC, 0xDD, 0xFF),
        Color(0xFF, 0xEE, 0xCC),
        Color(0xE8, 0xD0, 0xFF)
    )

    private fun getStarLayout(starConfig: StarConfig): Pair<List<StarPosition>, List<SparklePosition>> {
        return starCache.getOrPut(starConfig.seed) {
            val rng = Random(starConfig.seed)
            val totalDots = (starConfig.densityPerMillion * 1.2).toInt()

            val dots = (0 until totalDots).map {
                StarPosition(
                    nx = rng.nextDouble(),
                    ny = rng.nextDouble(),
                    size = starConfig.dotMinSize + rng.nextFloat() * (starConfig.dotMaxSize - starConfig.dotMinSize),
                    alpha = starConfig.dotMinOpacity + rng.nextFloat() * (starConfig.dotMaxOpacity - starConfig.dotMinOpacity),
                    colorIndex = rng.nextInt(STAR_PALETTE.size)
                )
            }

            val sparkles = (0 until starConfig.sparkleCount).map {
                SparklePosition(
                    nx = rng.nextDouble(),
                    ny = rng.nextDouble(),
                    colorIndex = rng.nextInt(STAR_PALETTE.size),
                    rayLength = 2 + rng.nextInt(3)
                )
            }

            Pair(dots, sparkles)
        }
    }

    fun drawStarField(
        g: Graphics2D,
        width: Int,
        height: Int,
        starConfig: StarConfig,
        @Suppress("UNUSED_PARAMETER") baseColor: Color
    ) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)

        val (dots, sparkles) = getStarLayout(starConfig)

        for (star in dots) {
            val x = (star.nx * width).toInt()
            val y = (star.ny * height).toInt()
            if (x < 2 || y < 2 || x >= width - 2 || y >= height - 2) continue
            val c = STAR_PALETTE[star.colorIndex]
            val a = (star.alpha * 255).toInt()

            if (star.size >= 1.4f) {
                g.color = Color(c.red, c.green, c.blue, a)
                g.fillRect(x, y, 1, 1)
                val dimA = (a * 0.4).toInt().coerceIn(0, 255)
                g.color = Color(c.red, c.green, c.blue, dimA)
                g.fillRect(x - 1, y, 1, 1)
                g.fillRect(x + 1, y, 1, 1)
                g.fillRect(x, y - 1, 1, 1)
                g.fillRect(x, y + 1, 1, 1)
            } else {
                g.color = Color(c.red, c.green, c.blue, a)
                g.fillRect(x, y, 1, 1)
            }
        }

        val ps = max(1, starConfig.sparkleSize.toInt())

        for (sparkle in sparkles) {
            val ix = (sparkle.nx * width).toInt()
            val iy = (sparkle.ny * height).toInt()
            val rl = sparkle.rayLength
            val margin = (rl + 1) * ps
            if (ix < margin || iy < margin || ix + margin >= width || iy + margin >= height) continue

            val c = STAR_PALETTE[sparkle.colorIndex]
            val fullA = (starConfig.sparkleOpacity * 255).toInt()

            g.color = Color(c.red, c.green, c.blue, fullA)
            g.fillRect(ix, iy, ps, ps)

            for (r in 1..rl) {
                val fade = ((1.0 - r.toDouble() / (rl + 1)) * fullA).toInt().coerceIn(0, 255)
                g.color = Color(c.red, c.green, c.blue, fade)
                g.fillRect(ix, iy - r * ps, ps, ps)
                g.fillRect(ix, iy + r * ps, ps, ps)
                g.fillRect(ix - r * ps, iy, ps, ps)
                g.fillRect(ix + r * ps, iy, ps, ps)
            }

            val diagLen = max(1, rl / 2)
            for (r in 1..diagLen) {
                val fade = ((1.0 - r.toDouble() / (diagLen + 1)) * fullA * 0.5).toInt().coerceIn(0, 255)
                g.color = Color(c.red, c.green, c.blue, fade)
                g.fillRect(ix - r * ps, iy - r * ps, ps, ps)
                g.fillRect(ix + r * ps, iy - r * ps, ps, ps)
                g.fillRect(ix - r * ps, iy + r * ps, ps, ps)
                g.fillRect(ix + r * ps, iy + r * ps, ps, ps)
            }
        }
    }

    fun drawMoonPhase(
        g: Graphics2D,
        cx: Double,
        cy: Double,
        radius: Double,
        phase: Double,
        color: Color,
        bgColor: Color
    ) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g.color = bgColor
        g.fill(Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2))

        g.color = color
        g.fill(Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2))

        val shadowRadius = radius * Math.abs(2.0 * phase - 1.0)
        val shadowCircle = Ellipse2D.Double(
            cx - shadowRadius, cy - radius,
            shadowRadius * 2, radius * 2
        )

        if (phase < 0.5) {
            val shadow = Area(shadowCircle)
            shadow.add(Area(Ellipse2D.Double(cx, cy - radius, radius, radius * 2)))
            g.color = bgColor
            g.fill(shadow)
        } else {
            val shadow = Area(Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2))
            val litArea = Area(shadowCircle)
            litArea.add(Area(Ellipse2D.Double(cx, cy - radius, radius, radius * 2)))
            shadow.subtract(litArea)
            g.color = bgColor
            g.fill(shadow)
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

    fun moonColorForTheme(colors: ColorConfig, logo: LogoConfig, theme: Theme): Color = when (theme) {
        Theme.DARK -> logo.moonGold
        Theme.LIGHT -> logo.moonGoldDark
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
            appendLine("""  <rect width="$size" height="$size" fill="$moonColor" mask="url(#crescent)"/>""")
            appendLine("</svg>")
        }
    }
}
