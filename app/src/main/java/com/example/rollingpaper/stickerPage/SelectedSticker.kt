package com.example.rollingpaper

import android.graphics.drawable.Drawable

data class SelectedSticker(
    val sticker: Drawable?,
    val offsetX: Float,
    val offsetY: Float,
    var deletable: Boolean = false
)
