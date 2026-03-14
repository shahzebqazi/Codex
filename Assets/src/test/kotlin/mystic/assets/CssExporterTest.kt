package mystic.assets

import mystic.assets.config.BrandConfig
import mystic.assets.exporters.CssExporter
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CssExporterTest {

    private lateinit var config: BrandConfig

    @BeforeAll
    fun setup() {
        val configPath = System.getProperty("brand.config") ?: "brand.yaml"
        config = BrandConfig.load(File(configPath))
    }

    @Test
    fun `generates CSS file with brand tokens`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

    @Test
    fun `CSS contains all primary color variables`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, "--mystic-primary:")
        assertContains(css, "--mystic-primary-dark:")
        assertContains(css, "--mystic-primary-light:")
    }

    @Test
    fun `CSS contains background variables`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, "--mystic-bg-dark:")
        assertContains(css, "--mystic-bg-surface:")
        assertContains(css, "--mystic-bg-panel:")
    }

    @Test
    fun `CSS contains typography variables`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, "--mystic-font-display:")
        assertContains(css, "--mystic-font-body:")
        assertContains(css, "--mystic-font-mono:")
        assertContains(css, "--mystic-tracking:")
    }

    @Test
    fun `CSS contains utility classes`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, ".mystic-text-primary")
        assertContains(css, ".mystic-bg-dark")
    }

    @Test
    fun `CSS contains hex values from config`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, ":root {")
        assertTrue(Regex("#[0-9A-F]{6}").containsMatchIn(css), "CSS should contain hex color values")
    }
}
