package dotai.assets.config

import org.yaml.snakeyaml.Yaml
import java.awt.Color
import java.io.File

data class BrandConfig(
    val name: String,
    val tagline: String,
    val colors: ColorConfig,
    val typography: TypographyConfig,
    val logo: LogoConfig,
    val icons: IconConfig,
    val logos: LogoVariantConfig,
    val banners: Map<String, SizeSpec>,
    val heroes: HeroConfig
) {
    companion object {
        fun load(file: File): BrandConfig {
            val yaml = Yaml()
            val data = yaml.load<Map<String, Any>>(file.inputStream())
            return parse(data)
        }

        @Suppress("UNCHECKED_CAST")
        private fun parse(data: Map<String, Any>): BrandConfig {
            val brand = data["brand"] as Map<String, Any>
            val colors = data["colors"] as Map<String, Any>
            val typo = data["typography"] as Map<String, Any>
            val logo = data["logo"] as Map<String, Any>
            val icons = data["icons"] as Map<String, Any>
            val logos = data["logos"] as Map<String, Any>
            val banners = data["banners"] as Map<String, Any>
            val heroes = data["heroes"] as Map<String, Any>

            return BrandConfig(
                name = brand["name"] as String,
                tagline = brand["tagline"] as String,
                colors = parseColors(colors),
                typography = parseTypography(typo),
                logo = parseLogo(logo),
                icons = parseIcons(icons),
                logos = parseLogos(logos),
                banners = parseSizeMap(banners),
                heroes = parseHeroes(heroes)
            )
        }

        @Suppress("UNCHECKED_CAST")
        private fun parseColors(c: Map<String, Any>): ColorConfig {
            val bg = c["background"] as Map<String, String>
            val fg = c["foreground"] as Map<String, String>
            val light = c["light"] as Map<String, String>
            val accent = c["accent"] as Map<String, String>
            return ColorConfig(
                primary = parseColor(c["primary"] as String),
                primaryDark = parseColor(c["primaryDark"] as String),
                primaryLight = parseColor(c["primaryLight"] as String),
                backgroundDark = parseColor(bg["dark"]!!),
                backgroundSurface = parseColor(bg["surface"]!!),
                backgroundPanel = parseColor(bg["panel"]!!),
                backgroundElevated = parseColor(bg["elevated"]!!),
                foregroundPrimary = parseColor(fg["primary"]!!),
                foregroundSecondary = parseColor(fg["secondary"]!!),
                foregroundMuted = parseColor(fg["muted"]!!),
                foregroundBorder = parseColor(fg["border"]!!),
                lightBackground = parseColor(light["background"]!!),
                lightForeground = parseColor(light["foreground"]!!),
                lightMuted = parseColor(light["muted"]!!),
                lightSurface = parseColor(light["surface"]!!),
                accentEmerald = parseColor(accent["emerald"]!!),
                accentAmber = parseColor(accent["amber"]!!),
                accentRed = parseColor(accent["red"]!!),
                accentYellow = parseColor(accent["yellow"]!!),
                accentGreen = parseColor(accent["green"]!!)
            )
        }

        @Suppress("UNCHECKED_CAST")
        private fun parseTypography(t: Map<String, Any>): TypographyConfig {
            val weights = t["weights"] as Map<String, Int>
            return TypographyConfig(
                display = t["display"] as String,
                body = t["body"] as String,
                mono = t["mono"] as String,
                tracking = (t["tracking"] as Number).toFloat(),
                weightNormal = weights["normal"]!!,
                weightMedium = weights["medium"]!!,
                weightBold = weights["bold"]!!
            )
        }

        private fun parseLogo(l: Map<String, Any>): LogoConfig = LogoConfig(
            style = l["style"] as String,
            moonCrescentRatio = (l["moonCrescentRatio"] as Number).toFloat(),
            moonOffsetX = (l["moonOffsetX"] as Number).toFloat(),
            moonOffsetY = (l["moonOffsetY"] as Number).toFloat(),
            glowRadius = (l["glowRadius"] as Number).toFloat(),
            glowOpacity = (l["glowOpacity"] as Number).toFloat()
        )

        @Suppress("UNCHECKED_CAST")
        private fun parseIcons(i: Map<String, Any>): IconConfig {
            val favicon = (i["favicon"] as List<Int>)
            val desktop = (i["desktop"] as List<Int>)
            val android = i["android"] as Map<String, Int>
            return IconConfig(
                faviconSizes = favicon,
                desktopSizes = desktop,
                androidForeground = android["foreground"]!!,
                androidBackground = android["background"]!!
            )
        }

        @Suppress("UNCHECKED_CAST")
        private fun parseLogos(l: Map<String, Any>): LogoVariantConfig {
            val social = l["social"] as Map<String, Map<String, Int>>
            val watermark = l["watermark"] as Map<String, Any>
            return LogoVariantConfig(
                variants = l["variants"] as List<String>,
                themes = l["themes"] as List<String>,
                socialSizes = social.mapValues { SizeSpec(it.value["width"]!!, it.value["height"]!!) },
                watermarkOpacity = (watermark["opacity"] as Number).toFloat()
            )
        }

        @Suppress("UNCHECKED_CAST")
        private fun parseSizeMap(m: Map<String, Any>): Map<String, SizeSpec> =
            m.mapValues { (_, v) ->
                val spec = v as Map<String, Int>
                SizeSpec(spec["width"]!!, spec["height"]!!)
            }

        @Suppress("UNCHECKED_CAST")
        private fun parseHeroes(h: Map<String, Any>): HeroConfig {
            val landing = h["landing"] as Map<String, Int>
            val retina = h["landingRetina"] as Map<String, Int>
            return HeroConfig(
                landing = SizeSpec(landing["width"]!!, landing["height"]!!),
                landingRetina = SizeSpec(retina["width"]!!, retina["height"]!!),
                rippleCount = h["rippleCount"] as Int,
                rippleSpacing = h["rippleSpacing"] as Int,
                rippleStrokeWidth = (h["rippleStrokeWidth"] as Number).toFloat(),
                rippleOpacity = (h["rippleOpacity"] as Number).toFloat()
            )
        }

        fun parseColor(hex: String): Color {
            if (hex.startsWith("rgba")) {
                val parts = hex.removePrefix("rgba(").removeSuffix(")").split(",").map { it.trim() }
                return Color(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), (parts[3].toFloat() * 255).toInt())
            }
            val h = hex.removePrefix("#")
            return when (h.length) {
                6 -> Color(h.substring(0, 2).toInt(16), h.substring(2, 4).toInt(16), h.substring(4, 6).toInt(16))
                8 -> Color(h.substring(0, 2).toInt(16), h.substring(2, 4).toInt(16), h.substring(4, 6).toInt(16), h.substring(6, 8).toInt(16))
                else -> Color.BLACK
            }
        }
    }
}

data class ColorConfig(
    val primary: Color,
    val primaryDark: Color,
    val primaryLight: Color,
    val backgroundDark: Color,
    val backgroundSurface: Color,
    val backgroundPanel: Color,
    val backgroundElevated: Color,
    val foregroundPrimary: Color,
    val foregroundSecondary: Color,
    val foregroundMuted: Color,
    val foregroundBorder: Color,
    val lightBackground: Color,
    val lightForeground: Color,
    val lightMuted: Color,
    val lightSurface: Color,
    val accentEmerald: Color,
    val accentAmber: Color,
    val accentRed: Color,
    val accentYellow: Color,
    val accentGreen: Color
)

data class TypographyConfig(
    val display: String,
    val body: String,
    val mono: String,
    val tracking: Float,
    val weightNormal: Int,
    val weightMedium: Int,
    val weightBold: Int
)

data class LogoConfig(
    val style: String,
    val moonCrescentRatio: Float,
    val moonOffsetX: Float,
    val moonOffsetY: Float,
    val glowRadius: Float,
    val glowOpacity: Float
)

data class IconConfig(
    val faviconSizes: List<Int>,
    val desktopSizes: List<Int>,
    val androidForeground: Int,
    val androidBackground: Int
)

data class LogoVariantConfig(
    val variants: List<String>,
    val themes: List<String>,
    val socialSizes: Map<String, SizeSpec>,
    val watermarkOpacity: Float
)

data class SizeSpec(val width: Int, val height: Int)

data class HeroConfig(
    val landing: SizeSpec,
    val landingRetina: SizeSpec,
    val rippleCount: Int,
    val rippleSpacing: Int,
    val rippleStrokeWidth: Float,
    val rippleOpacity: Float
)
