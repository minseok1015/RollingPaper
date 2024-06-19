package com.example.rollingpaper.component

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.rollingpaper.R

data class Fonts(
    val name: String,
    val font: FontFamily
) {
    companion object {
        // Compose FontFamily 객체를 포함하는 배열을 정의합니다.
        val fontArray = arrayOf(
            Fonts(name = "산토끼", font = FontFamily(Font(R.font.santokki))),
            Fonts(name = "눈내리는", font = FontFamily(Font(R.font.snow))),
            Fonts(name = "본고딕", font = FontFamily(Font(R.font.noto))),
            Fonts(name = "한나체", font = FontFamily(Font(R.font.baemin)))
            // 다른 폰트들도 추가할 수 있습니다.
        )

        // 인덱스를 통해 Compose FontFamily 객체를 반환하는 메서드
        fun getFontByIndex(index: Int): FontFamily {
            return if (index in fontArray.indices) {
                fontArray[index].font
            } else {
                fontArray[0].font // 기본 폰트 또는 인덱스 범위를 벗어난 경우 처리
            }
        }
    }
}
