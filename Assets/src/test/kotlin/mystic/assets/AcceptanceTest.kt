package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.exporters.CssExporter
import mystic.assets.exporters.HtmlExporter
import mystic.assets.exporters.PngExporter
import mystic.assets.exporters.SvgExporter
import mystic.assets.generators.BannerGenerator
import mystic.assets.generators.HeroGenerator
import mystic.assets.generators.IconGenerator
import mystic.assets.generators.LogoGenerator
import mystic.assets.generators.PixelIconGenerator
import mystic.assets.generators.PixelLogoGenerator
import mystic.assets.model.Asset
import mystic.assets.model.AssetFormat
import mystic.assets.model.Theme
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AcceptanceTest {

    private lateinit var config: BrandConfig
    private lateinit var allAssets: List<Asset>

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
        allAssets = IconGenerator(config).generate() +
                LogoGenerator(config).generate() +
                BannerGenerator(config).generate() +
                HeroGenerator(config).generate() +
                PixelIconGenerator(config).generate() +
                PixelLogoGenerator(config).generate()
    }

    @Test
    fun `full pipeline produces at least 70 assets`() {
        assertTrue(allAssets.size >= 70, "Expected at least 70 assets (including pixel), got ${allAssets.size}")
    }

    @Test
    fun `all PNG assets have valid image data`() {
        val pngAssets = allAssets.filter { it.format == AssetFormat.PNG }
        for (asset in pngAssets) {
            assertNotNull(asset.image, "PNG asset ${asset.name} missing image data")
            assertTrue(asset.image!!.width > 0, "Image width should be positive")
            assertTrue(asset.image!!.height > 0, "Image height should be positive")
        }
    }

    @Test
    fun `all SVG assets have valid content`() {
        val svgAssets = allAssets.filter { it.format == AssetFormat.SVG }
        for (asset in svgAssets) {
            assertNotNull(asset.svgContent, "SVG asset ${asset.name} missing content")
            assertTrue(asset.svgContent!!.startsWith("<svg"), "SVG should start with <svg tag")
            assertTrue(asset.svgContent!!.contains("</svg>"), "SVG should close properly")
        }
    }

    @Test
    fun `exported PNGs can be read back`(@TempDir tempDir: File) {
        val files = PngExporter(tempDir).export(allAssets)
        assertTrue(files.isNotEmpty())
        for (file in files) {
            assertTrue(file.exists(), "File should exist: ${file.name}")
            assertTrue(file.length() > 0, "File should not be empty: ${file.name}")
            val img = ImageIO.read(file)
            assertNotNull(img, "Should be able to read PNG: ${file.name}")
        }
    }

    @Test
    fun `exported SVGs are valid XML`(@TempDir tempDir: File) {
        val files = SvgExporter(tempDir).export(allAssets)
        assertTrue(files.isNotEmpty())
        for (file in files) {
            val content = file.readText()
            assertTrue(content.contains("<svg"), "SVG file should contain svg element: ${file.name}")
            assertTrue(content.contains("xmlns"), "SVG should have xmlns: ${file.name}")
        }
    }

    @Test
    fun `dark theme icons use dark background`() {
        val darkIcons = allAssets.filter {
            it.format == AssetFormat.PNG && it.type == mystic.assets.model.AssetType.ICON
                    && it.theme == Theme.DARK && it.image != null && it.width >= 32
        }
        for (icon in darkIcons) {
            val cornerPixel = Color(icon.image!!.getRGB(0, 0), true)
            assertTrue(cornerPixel.red <= 30 && cornerPixel.green <= 30 && cornerPixel.blue <= 30,
                "Dark icon ${icon.name} corner should be near-black, got RGB(${cornerPixel.red},${cornerPixel.green},${cornerPixel.blue})")
        }
    }

    @Test
    fun `light theme icons use light background`() {
        val lightIcons = allAssets.filter {
            it.format == AssetFormat.PNG && it.type == mystic.assets.model.AssetType.ICON
                    && it.theme == Theme.LIGHT && it.image != null && it.width >= 32
        }
        for (icon in lightIcons) {
            val cornerPixel = Color(icon.image!!.getRGB(0, 0), true)
            assertTrue(cornerPixel.red >= 240 && cornerPixel.green >= 240 && cornerPixel.blue >= 230,
                "Light icon ${icon.name} corner should be near-white, got RGB(${cornerPixel.red},${cornerPixel.green},${cornerPixel.blue})")
        }
    }

    @Test
    fun `gold moon color appears in dark theme icons`() {
        val goldColor = config.logo.moonGold
        val darkIcons = allAssets.filter {
            it.format == AssetFormat.PNG && it.type == mystic.assets.model.AssetType.ICON
                    && it.theme == Theme.DARK && it.image != null && it.width >= 64
        }

        for (icon in darkIcons) {
            val img = icon.image!!
            var foundGold = false
            for (y in 0 until img.height) {
                for (x in 0 until img.width) {
                    val pixel = Color(img.getRGB(x, y), true)
                    if (colorDistance(pixel, goldColor) < 40) {
                        foundGold = true
                        break
                    }
                }
                if (foundGold) break
            }
            assertTrue(foundGold, "Dark icon ${icon.name} should contain gold moon color")
        }
    }

    @Test
    fun `WCAG contrast ratio between text and background exceeds 4_5 to 1`() {
        val bgLum = relativeLuminance(config.colors.backgroundDark)
        val fgLum = relativeLuminance(config.colors.foregroundPrimary)
        val ratio = contrastRatio(bgLum, fgLum)
        assertTrue(ratio >= 4.5,
            "Text contrast ratio should be >= 4.5:1, got ${"%.2f".format(ratio)}:1")
    }

    @Test
    fun `WCAG contrast ratio for UI elements exceeds 3 to 1`() {
        val bgLum = relativeLuminance(config.colors.backgroundDark)
        val accentLum = relativeLuminance(config.colors.primary)
        val ratio = contrastRatio(bgLum, accentLum)
        assertTrue(ratio >= 3.0,
            "UI element contrast ratio should be >= 3:1, got ${"%.2f".format(ratio)}:1")
    }

    @Test
    fun `CSS export contains all brand variables`(@TempDir tempDir: File) {
        val cssFile = CssExporter(tempDir).export(config)
        val css = cssFile.readText()

        val requiredVars = listOf(
            "--mystic-primary", "--mystic-bg-dark", "--mystic-bg-surface",
            "--mystic-fg-primary", "--mystic-fg-muted", "--mystic-font-display",
            "--mystic-font-mono", "--mystic-tracking"
        )
        for (v in requiredVars) {
            assertContains(css, v, message = "CSS should contain $v")
        }
    }

    @Test
    fun `HTML preview is well-formed`(@TempDir tempDir: File) {
        val htmlFile = HtmlExporter(tempDir).export(allAssets, config)
        val html = htmlFile.readText()

        assertContains(html, "<!DOCTYPE html>")
        assertContains(html, "<head>")
        assertContains(html, "</head>")
        assertContains(html, "<body>")
        assertContains(html, "</body>")
        assertContains(html, "brand.css")

        val imgCount = Regex("<img ").findAll(html).count()
        assertTrue(imgCount > 0, "HTML should reference at least one image")
    }

    @Test
    fun `full pipeline export produces complete file tree`(@TempDir tempDir: File) {
        PngExporter(tempDir).export(allAssets)
        SvgExporter(tempDir).export(allAssets)
        CssExporter(tempDir).export(config)
        HtmlExporter(tempDir).export(allAssets, config)

        assertTrue(File(tempDir, "icons").exists(), "icons/ dir should exist")
        assertTrue(File(tempDir, "logos").exists(), "logos/ dir should exist")
        assertTrue(File(tempDir, "banners").exists(), "banners/ dir should exist")
        assertTrue(File(tempDir, "heroes").exists(), "heroes/ dir should exist")
        assertTrue(File(tempDir, "css/brand.css").exists(), "css/brand.css should exist")
        assertTrue(File(tempDir, "preview/index.html").exists(), "preview/index.html should exist")
    }

    private fun colorDistance(a: Color, b: Color): Double {
        val dr = (a.red - b.red).toDouble()
        val dg = (a.green - b.green).toDouble()
        val db = (a.blue - b.blue).toDouble()
        return Math.sqrt(dr * dr + dg * dg + db * db)
    }

    private fun relativeLuminance(c: Color): Double {
        fun channel(v: Int): Double {
            val s = v / 255.0
            return if (s <= 0.03928) s / 12.92 else ((s + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * channel(c.red) + 0.7152 * channel(c.green) + 0.0722 * channel(c.blue)
    }

    private fun contrastRatio(l1: Double, l2: Double): Double {
        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05) / (darker + 0.05)
    }
}
