package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.generators.IconGenerator
import mystic.assets.model.AssetFormat
import mystic.assets.model.AssetType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IconGeneratorTest {

    private lateinit var config: BrandConfig
    private val generator by lazy { IconGenerator(config) }

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates icons for all desktop sizes and themes`() {
        val assets = generator.generate()
        val pngIcons = assets.filter { it.format == AssetFormat.PNG && it.name.startsWith("icon-") }

        val expectedDesktopCount = config.icons.desktopSizes.size * 3
        val desktopIcons = pngIcons.filter { !it.name.startsWith("favicon") }
        assertEquals(expectedDesktopCount, desktopIcons.size,
            "Expected $expectedDesktopCount desktop icons (${config.icons.desktopSizes.size} sizes x 3 themes)")
    }

    @Test
    fun `generates favicons`() {
        val assets = generator.generate()
        val favicons = assets.filter { it.name.startsWith("favicon") }

        assertEquals(config.icons.faviconSizes.size, favicons.size)
        for (favicon in favicons) {
            assertTrue(favicon.width <= 48, "Favicon should be 48px or smaller")
        }
    }

    @Test
    fun `generates SVG icons`() {
        val assets = generator.generate()
        val svgIcons = assets.filter { it.format == AssetFormat.SVG }

        assertEquals(3, svgIcons.size, "Should have 3 SVG icons (one per theme)")
        for (svg in svgIcons) {
            assertNotNull(svg.svgContent)
            assertTrue(svg.svgContent!!.contains("<svg"), "SVG content should be valid")
            assertTrue(svg.svgContent!!.contains("crescent"), "SVG should contain crescent mask")
        }
    }

    @Test
    fun `all icons are square`() {
        val assets = generator.generate()
        for (asset in assets.filter { it.format == AssetFormat.PNG }) {
            assertEquals(asset.width, asset.height, "Icon ${asset.name} should be square")
            assertNotNull(asset.image, "Icon ${asset.name} should have image data")
            assertEquals(asset.width, asset.image!!.width, "Image width should match declared width")
        }
    }

    @Test
    fun `all generated icons are of type ICON`() {
        val assets = generator.generate()
        assertTrue(assets.all { it.type == AssetType.ICON })
    }
}
