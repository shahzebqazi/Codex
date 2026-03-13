package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.exporters.HtmlExporter
import mystic.assets.generators.IconGenerator
import mystic.assets.generators.LogoGenerator
import mystic.assets.model.Asset
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HtmlExporterTest {

    private lateinit var config: BrandConfig
    private lateinit var sampleAssets: List<Asset>

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
        sampleAssets = IconGenerator(config).generate() + LogoGenerator(config).generate()
    }

    @Test
    fun `generates HTML preview file`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

    @Test
    fun `HTML contains doctype and structure`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "<!DOCTYPE html>")
        assertContains(html, "<html")
        assertContains(html, "</html>")
        assertContains(html, "Mystic Brand Guide")
    }

    @Test
    fun `HTML references CSS brand file`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "brand.css")
    }

    @Test
    fun `HTML contains top-level sections`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "Overview")
        assertContains(html, "Index")
        assertContains(html, "Brand Guide")
        assertContains(html, "Research")
    }

    @Test
    fun `HTML contains top navigation bar`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "top-nav")
        assertContains(html, "Mockups")
        assertContains(html, "top-nav-links")
    }

    @Test
    fun `HTML contains header logo and overview`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "brand-header")
        assertContains(html, "header-logo")
        assertContains(html, "overview-content")
        assertContains(html, "./gradlew run")
    }

    @Test
    fun `HTML contains asset sections`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "Icon Suite")
        assertContains(html, "Logo Suite")
    }

    @Test
    fun `HTML contains color swatches`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "swatch")
        assertContains(html, "Moon Phases")
    }

    @Test
    fun `HTML references image files with relative paths`(@TempDir tempDir: File) {
        val file = HtmlExporter(tempDir).export(sampleAssets, config)
        val html = file.readText()
        assertContains(html, "../icons/")
        assertContains(html, "../logos/")
        assertContains(html, ".png")
    }
}
