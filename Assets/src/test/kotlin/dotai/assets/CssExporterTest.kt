package dotai.assets

import dotai.assets.config.BrandConfig
import dotai.assets.exporters.CssExporter
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
        assertContains(css, "--dotai-primary:")
        assertContains(css, "--dotai-primary-dark:")
        assertContains(css, "--dotai-primary-light:")
    }

    @Test
    fun `CSS contains background variables`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, "--dotai-bg-dark:")
        assertContains(css, "--dotai-bg-surface:")
        assertContains(css, "--dotai-bg-panel:")
    }

    @Test
    fun `CSS contains typography variables`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, "--dotai-font-display:")
        assertContains(css, "--dotai-font-body:")
        assertContains(css, "--dotai-font-mono:")
        assertContains(css, "--dotai-tracking:")
    }

    @Test
    fun `CSS contains utility classes`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, ".dotai-moon-glow")
        assertContains(css, ".dotai-text-primary")
        assertContains(css, ".dotai-bg-dark")
    }

    @Test
    fun `CSS contains correct hex values from config`(@TempDir tempDir: File) {
        val file = CssExporter(tempDir).export(config)
        val css = file.readText()
        assertContains(css, "#0EA5E9")
        assertContains(css, "#000000")
    }
}
