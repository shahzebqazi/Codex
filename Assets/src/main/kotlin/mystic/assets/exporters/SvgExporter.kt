package mystic.assets.exporters

import mystic.assets.model.Asset
import mystic.assets.model.AssetFormat
import java.io.File

class SvgExporter(private val outputDir: File) {

    fun export(assets: List<Asset>): List<File> {
        return assets
            .filter { it.format == AssetFormat.SVG && it.svgContent != null }
            .map { asset ->
                val dir = File(outputDir, asset.subdirectory())
                dir.mkdirs()
                val file = File(dir, asset.filename())
                file.writeText(asset.svgContent!!)
                file
            }
    }
}
