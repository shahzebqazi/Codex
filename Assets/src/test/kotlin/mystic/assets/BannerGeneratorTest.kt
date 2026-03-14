package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.generators.BannerGenerator
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
class BannerGeneratorTest {

    private lateinit var config: BrandConfig
    private val generator by lazy { BannerGenerator(config) }

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates one banner per configured size`() {
        val assets = generator.generate()
        assertEquals(config.banners.size, assets.size)
    }

    @Test
    fun `banners match configured dimensions`() {
        val assets = generator.generate()
        for (asset in assets) {
            assertNotNull(asset.image)
            assertEquals(asset.width, asset.image!!.width)
            assertEquals(asset.height, asset.image!!.height)
        }
    }

    @Test
    fun `github banner is 1280x640`() {
        val assets = generator.generate()
        val github = assets.find { it.name == "banner-github" }
        assertNotNull(github, "Should have github banner")
        assertEquals(1280, github.width)
        assertEquals(640, github.height)
    }

    @Test
    fun `all banners are landscape orientation`() {
        val assets = generator.generate()
        for (asset in assets) {
            assertTrue(asset.width > asset.height, "Banner ${asset.name} should be landscape")
        }
    }

    @Test
    fun `all generated banners are of type BANNER`() {
        val assets = generator.generate()
        assertTrue(assets.all { it.type == AssetType.BANNER })
        assertTrue(assets.all { it.format == AssetFormat.PNG })
    }
}
