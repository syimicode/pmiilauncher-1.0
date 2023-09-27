package com.syimicode.pmiilauncher.model

data class WallpaperModel(
    var id: String? = null,
    var link: String? = null,
    var name: String? = null,
    var sekretariat: String? = null,
    var checkFavorite: Boolean? = false,
    var userId: List<String>? = null
) : java.io.Serializable