package com.example.rollingpaper.component

import androidx.compose.ui.graphics.Color

data class FontColors(val name: String,
                      val color: Color){
    companion object {
        // Compose Color 객체를 포함하는 배열을 정의합니다.
        private val fontColorsArray = arrayOf(
            Colors(name = "Red", color = Color(0xFFFF0000)),
            Colors(name = "Green", color = Color(0xFF00FF00)),
            Colors(name = "Blue", color = Color(0xFF0000FF))
            // 다른 색상들도 추가할 수 있습니다.
        )

        // 인덱스를 통해 Compose Color 객체를 반환하는 메서드
        fun getFontColorByIndex(index: Int): Color {
            return if (index in fontColorsArray.indices) {
                fontColorsArray[index].color
            } else {
                fontColorsArray[0].color // 기본 색상 또는 인덱스 범위를 벗어난 경우 처리
            }
        }
    }
}
