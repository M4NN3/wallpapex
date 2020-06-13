package com.mnn.wallpapex

import com.google.firebase.Timestamp

data class WallpapersModel (
    val characterId: Int = -1,
    val wpURL: String = "",
    val tmbURL: String = "",
    val date: Timestamp? = null
)