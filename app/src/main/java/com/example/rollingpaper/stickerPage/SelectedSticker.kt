package com.example.rollingpaper

import android.graphics.drawable.Drawable

data class SelectedSticker(
    val sticker: Drawable?,
    var offsetX: Float,
    var offsetY: Float,
    var deletable: Boolean = false
)
