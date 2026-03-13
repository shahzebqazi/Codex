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
import java.io.File

fun main(args: Array<String>) {
    val configPath: String
    val outputPath: String

    if (args.size >= 4 && args[0] == "--config" && args[2] == "--output") {
        configPath = args[1]
        outputPath = args[3]
    } else {
        configPath = "brand.yaml"
        outputPath = "output"
    }

    val configFile = File(configPath)
    require(configFile.exists()) { "Brand config not found: $configPath" }

    val outputDir = File(outputPath)
    outputDir.mkdirs()

    println("Mystic Asset Generator")
    println("======================")
    println("Config: ${configFile.absolutePath}")
    println("Output: ${outputDir.absolutePath}")
    println()

    val config = BrandConfig.load(configFile)
    println("Brand: ${config.name}")
    println("Style: ${config.logo.style}")
    println()

    println("Generating icons...")
    val iconAssets = IconGenerator(config).generate()
    println("  ${iconAssets.size} icon assets")

    println("Generating logos...")
    val logoAssets = LogoGenerator(config).generate()
    println("  ${logoAssets.size} logo assets")

    println("Generating banners...")
    val bannerAssets = BannerGenerator(config).generate()
    println("  ${bannerAssets.size} banner assets")

    println("Generating heroes...")
    val heroAssets = HeroGenerator(config).generate()
    println("  ${heroAssets.size} hero assets")

    println("Generating pixel-art icons...")
    val pixelIconAssets = PixelIconGenerator(config).generate()
    println("  ${pixelIconAssets.size} pixel icon assets")

    println("Generating pixel-art logos...")
    val pixelLogoAssets = PixelLogoGenerator(config).generate()
    println("  ${pixelLogoAssets.size} pixel logo assets")

    val allAssets = iconAssets + logoAssets + bannerAssets + heroAssets + pixelIconAssets + pixelLogoAssets
    println()
    println("Total: ${allAssets.size} assets generated")
    println()

    println("Exporting PNGs...")
    val pngFiles = PngExporter(outputDir).export(allAssets)
    println("  ${pngFiles.size} PNG files written")

    println("Exporting SVGs...")
    val svgFiles = SvgExporter(outputDir).export(allAssets)
    println("  ${svgFiles.size} SVG files written")

    println("Exporting CSS brand tokens...")
    val cssFile = CssExporter(outputDir).export(config)
    println("  Written: ${cssFile.name}")

    println("Generating HTML preview...")
    val htmlFile = HtmlExporter(outputDir).export(allAssets, config)
    println("  Written: ${htmlFile.name}")

    println()
    println("Done! Open ${htmlFile.absolutePath} to preview all assets.")
    println()

    println("Asset manifest:")
    println("───────────────")
    for (asset in allAssets) {
        println("  ${asset.subdirectory()}/${asset.filename()} (${asset.width}x${asset.height} ${asset.format})")
    }
}
