package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.generators.HeroGenerator
import mystic.assets.model.AssetType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HeroGeneratorTest {

    private lateinit var config: BrandConfig
    private val generator by lazy { HeroGenerator(config) }

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates landing and retina heroes`() {
        val assets = generator.generate()
        assertEquals(2, assets.size, "Should have landing and retina hero")
    }

    @Test
    fun `landing hero matches configured dimensions`() {
        val assets = generator.generate()
        val landing = assets.find { it.name == "hero-landing" }
        assertNotNull(landing)
        assertEquals(config.heroes.landing.width, landing.width)
        assertEquals(config.heroes.landing.height, landing.height)
        assertNotNull(landing.image)
        assertEquals(landing.width, landing.image!!.width)
    }

    @Test
    fun `retina hero is larger than standard`() {
        val assets = generator.generate()
        val landing = assets.find { it.name == "hero-landing" }!!
        val retina = assets.find { it.name == "hero-landing-retina" }!!
        assertTrue(retina.width > landing.width)
        assertTrue(retina.height > landing.height)
    }

    @Test
    fun `all generated heroes are of type HERO`() {
        val assets = generator.generate()
        assertTrue(assets.all { it.type == AssetType.HERO })
    }
}
