package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.generators.PixelIconGenerator
import mystic.assets.model.AssetFormat
import mystic.assets.model.AssetType
import mystic.assets.model.Theme
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.awt.Color
import java.io.File
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PixelIconGeneratorTest {

    private lateinit var config: BrandConfig
    private val generator by lazy { PixelIconGenerator(config) }

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates pixel icons for all configured sizes and themes`() {
        val assets = generator.generate()
        val expectedCount = config.pixelArt.sizes.size * Theme.entries.size
        assertEquals(expectedCount, assets.size,
            "Expected $expectedCount pixel icons (${config.pixelArt.sizes.size} sizes x ${Theme.entries.size} themes)")
    }

    @Test
    fun `all pixel icons are PNG and PIXEL_ICON type`() {
        val assets = generator.generate()
        assertTrue(assets.all { it.format == AssetFormat.PNG && it.type == AssetType.PIXEL_ICON })
    }

    @Test
    fun `all pixel icons are square`() {
        val assets = generator.generate()
        for (asset in assets) {
            assertEquals(asset.width, asset.height, "Pixel icon ${asset.name} should be square")
            assertNotNull(asset.image)
            assertEquals(asset.width, asset.image!!.width)
            assertEquals(asset.height, asset.image!!.height)
        }
    }

    @Test
    fun `dark theme pixel icons have near-black corner`() {
        val assets = generator.generate().filter { it.theme == Theme.DARK }
        for (icon in assets) {
            val cornerPixel = Color(icon.image!!.getRGB(0, 0), true)
            assertTrue(
                cornerPixel.red <= 30 && cornerPixel.green <= 30 && cornerPixel.blue <= 30,
                "Dark pixel icon ${icon.name} corner should be near-black, got RGB(${cornerPixel.red},${cornerPixel.green},${cornerPixel.blue})"
            )
        }
    }

    @Test
    fun `light theme pixel icons have near-white corner`() {
        val assets = generator.generate().filter { it.theme == Theme.LIGHT }
        for (icon in assets) {
            val cornerPixel = Color(icon.image!!.getRGB(0, 0), true)
            assertTrue(
                cornerPixel.red >= 240 && cornerPixel.green >= 240 && cornerPixel.blue >= 230,
                "Light pixel icon ${icon.name} corner should be near-white, got RGB(${cornerPixel.red},${cornerPixel.green},${cornerPixel.blue})"
            )
        }
    }

    @Test
    fun `pixel icons contain gold moon color`() {
        val goldColor = Color(0xFA, 0xD0, 0x75)
        val assets = generator.generate().filter { it.theme == Theme.DARK }
        for (icon in assets) {
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
            assertTrue(foundGold, "Dark pixel icon ${icon.name} should contain gold moon color")
        }
    }

    private fun colorDistance(a: Color, b: Color): Double {
        val dr = (a.red - b.red).toDouble()
        val dg = (a.green - b.green).toDouble()
        val db = (a.blue - b.blue).toDouble()
        return sqrt(dr * dr + dg * dg + db * db)
    }
}
