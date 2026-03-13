package dotai.assets

import dotai.assets.config.BrandConfig
import dotai.assets.exporters.CssExporter
import dotai.assets.exporters.HtmlExporter
import dotai.assets.exporters.PngExporter
import dotai.assets.exporters.SvgExporter
import dotai.assets.generators.BannerGenerator
import dotai.assets.generators.HeroGenerator
import dotai.assets.generators.IconGenerator
import dotai.assets.generators.LogoGenerator
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

    println("dotAi Asset Generator")
    println("=====================")
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

    val allAssets = iconAssets + logoAssets + bannerAssets + heroAssets
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
