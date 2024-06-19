package com.example.rollingpaper.component


import androidx.compose.ui.graphics.Color

data class Colors(
    val name: String,
    val color: Color
) {
    companion object {
        // Compose Color 객체를 포함하는 배열을 정의합니다.
        val colorsArray = arrayOf(
            Colors(name = "Black", color = Color(0xFF000000)), // Black
            Colors(name = "Red", color = Color(0xFFFFB3BA)), // Pastel Red
            Colors(name = "Green", color = Color(0xFFB4E4B5)), // Pastel Green
            Colors(name = "Blue", color = Color(0xFFAEC6CF)), // Pastel Blue
            Colors(name = "Yellow", color = Color(0xFFFFF9A6)), // Pastel Yellow
            Colors(name = "Purple", color = Color(0xFFC9ACE6)), // Pastel Purple
            Colors(name = "Pink", color = Color(0xFFFFC1CC)), // Pastel Pink
            Colors(name = "Orange", color = Color(0xFFFFD1B2)) // Pastel Orange
        )

        // 인덱스를 통해 Compose Color 객체를 반환하는 메서드
        fun getColorByIndex(index: Int): Color {
            return if (index in colorsArray.indices) {
                colorsArray[index].color
            } else {
                colorsArray[0].color // 기본 색상 또는 인덱스 범위를 벗어난 경우 처리
            }
        }
    }
}
