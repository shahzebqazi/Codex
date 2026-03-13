package mystic.assets.exporters

import mystic.assets.model.Asset
import mystic.assets.model.AssetFormat
import java.io.File
import javax.imageio.ImageIO

class PngExporter(private val outputDir: File) {

    fun export(assets: List<Asset>): List<File> {
        return assets
            .filter { it.format == AssetFormat.PNG && it.image != null }
            .map { asset ->
                val dir = File(outputDir, asset.subdirectory())
                dir.mkdirs()
                val file = File(dir, asset.filename())
                ImageIO.write(asset.image!!, "PNG", file)
                file
            }
    }
}
