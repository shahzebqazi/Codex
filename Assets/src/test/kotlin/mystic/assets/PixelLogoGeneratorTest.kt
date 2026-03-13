package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.generators.PixelLogoGenerator
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
class PixelLogoGeneratorTest {

    private lateinit var config: BrandConfig
    private val generator by lazy { PixelLogoGenerator(config) }

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates pixel logos for icon-only and wordmark`() {
        val assets = generator.generate()
        val iconOnlyCount = config.pixelArt.sizes.size * Theme.entries.size
        val wordmarkCount = Theme.entries.size
        val expectedCount = iconOnlyCount + wordmarkCount
        assertEquals(expectedCount, assets.size)
    }

    @Test
    fun `all pixel logos are PNG and PIXEL_LOGO type`() {
        val assets = generator.generate()
        assertTrue(assets.all { it.format == AssetFormat.PNG && it.type == AssetType.PIXEL_LOGO })
    }

    @Test
    fun `icon-only pixel logos are square`() {
        val assets = generator.generate().filter { it.name.contains("icon-only") }
        for (asset in assets) {
            assertEquals(asset.width, asset.height, "Icon-only pixel logo ${asset.name} should be square")
            assertNotNull(asset.image)
        }
    }

    @Test
    fun `wordmark pixel logos have correct dimensions`() {
        val assets = generator.generate().filter { it.name.contains("wordmark") }
        assertEquals(Theme.entries.size, assets.size)
        for (asset in assets) {
            assertEquals(160, asset.width)
            assertEquals(40, asset.height)
            assertNotNull(asset.image)
        }
    }

    @Test
    fun `all pixel logos have valid image data`() {
        val assets = generator.generate()
        for (asset in assets) {
            assertNotNull(asset.image)
            assertTrue(asset.image!!.width > 0)
            assertTrue(asset.image!!.height > 0)
        }
    }
}
