package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.generators.LogoGenerator
import mystic.assets.model.AssetFormat
import mystic.assets.model.AssetType
import mystic.assets.model.Theme
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogoGeneratorTest {

    private lateinit var config: BrandConfig
    private val generator by lazy { LogoGenerator(config) }

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates all variant-theme combinations`() {
        val assets = generator.generate()
        val pngLogos = assets.filter { it.format == AssetFormat.PNG && it.name.startsWith("logo-") && !it.name.contains("social") && !it.name.contains("watermark") }

        val expectedCount = config.logos.variants.size * config.logos.themes.size
        assertEquals(expectedCount, pngLogos.size,
            "Expected $expectedCount logos (${config.logos.variants.size} variants x ${config.logos.themes.size} themes)")
    }

    @Test
    fun `generates social media logos`() {
        val assets = generator.generate()
        val socialLogos = assets.filter { it.name.contains("social") }

        assertEquals(config.logos.socialSizes.size, socialLogos.size)
        for ((name, spec) in config.logos.socialSizes) {
            val logo = socialLogos.find { it.name.contains(name) }
            assertNotNull(logo, "Should have social logo for $name")
            assertEquals(spec.width, logo.width)
            assertEquals(spec.height, logo.height)
        }
    }

    @Test
    fun `generates watermark`() {
        val assets = generator.generate()
        val watermark = assets.find { it.name.contains("watermark") }

        assertNotNull(watermark, "Should generate watermark")
        assertNotNull(watermark.image)
    }

    @Test
    fun `generates SVG logos for all variant-theme combinations`() {
        val assets = generator.generate()
        val svgLogos = assets.filter { it.format == AssetFormat.SVG }

        val expectedCount = config.logos.variants.size * config.logos.themes.size
        assertEquals(expectedCount, svgLogos.size)

        for (svg in svgLogos) {
            assertNotNull(svg.svgContent)
            assertTrue(svg.svgContent!!.contains("<svg"))
        }
    }

    @Test
    fun `full logo is wider than tall`() {
        val assets = generator.generate()
        val fullLogos = assets.filter { it.name.startsWith("logo-full") && it.format == AssetFormat.PNG }

        for (logo in fullLogos) {
            assertTrue(logo.width > logo.height, "Full logo should be landscape orientation")
        }
    }

    @Test
    fun `all generated logos are of type LOGO`() {
        val assets = generator.generate()
        assertTrue(assets.all { it.type == AssetType.LOGO })
    }
}
