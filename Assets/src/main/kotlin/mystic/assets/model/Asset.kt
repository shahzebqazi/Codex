package mystic.assets.model

import java.awt.image.BufferedImage

enum class AssetType {
    ICON, LOGO, BANNER, HERO, PIXEL_ICON, PIXEL_LOGO
}

enum class AssetFormat {
    PNG, SVG, CSS, HTML, ICO
}

enum class Theme {
    DARK, LIGHT, MONO
}

data class Asset(
    val type: AssetType,
    val format: AssetFormat,
    val name: String,
    val width: Int,
    val height: Int,
    val theme: Theme = Theme.DARK,
    val image: BufferedImage? = null,
    val svgContent: String? = null,
    val textContent: String? = null
) {
    fun filename(): String = when (format) {
        AssetFormat.PNG -> "$name.png"
        AssetFormat.SVG -> "$name.svg"
        AssetFormat.CSS -> "$name.css"
        AssetFormat.HTML -> "$name.html"
        AssetFormat.ICO -> "$name.ico"
    }

    fun subdirectory(): String = when (type) {
        AssetType.ICON -> "icons"
        AssetType.LOGO -> "logos"
        AssetType.BANNER -> "banners"
        AssetType.HERO -> "heroes"
        AssetType.PIXEL_ICON -> "icons/pixel"
        AssetType.PIXEL_LOGO -> "logos/pixel"
    }
}
