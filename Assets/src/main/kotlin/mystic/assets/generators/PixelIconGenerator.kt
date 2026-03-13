package mystic.assets.generators

import mystic.assets.config.BrandConfig
import mystic.assets.model.Asset
import mystic.assets.model.AssetFormat
import mystic.assets.model.AssetType
import mystic.assets.model.Theme
import mystic.assets.model.PixelArtRenderer

class PixelIconGenerator(private val config: BrandConfig) {

    fun generate(): List<Asset> {
        val assets = mutableListOf<Asset>()

        for (size in config.pixelArt.sizes) {
            for (theme in Theme.entries) {
                val img = PixelArtRenderer.renderPixelIcon(size, theme, config)
                assets.add(
                    Asset(
                        type = AssetType.PIXEL_ICON,
                        format = AssetFormat.PNG,
                        name = "icon-${theme.name.lowercase()}-${size}x${size}",
                        width = size,
                        height = size,
                        theme = theme,
                        image = img
                    )
                )
            }
        }

        return assets
    }
}
