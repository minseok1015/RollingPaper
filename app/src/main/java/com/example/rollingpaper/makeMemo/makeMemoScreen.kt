package com.example.rollingpaper.makeMemo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.R
import kotlin.math.abs

@Composable
fun makeMemoScreen(navController: NavController) {
    var text by remember { mutableStateOf("") }
    var anonymous by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var showTextAlignDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16.sp) } // 기본 글씨 크기
    var textAlign by remember { mutableStateOf(TextAlign.Start) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var author by remember { mutableStateOf("") }

    val fontFamilies = listOf("굴림체", "궁서체", "바탕체", "고딕체")
    var selectedFont by remember { mutableStateOf(fontFamilies[0]) }

    if (showFontSizeDialog) {
        FontSizeDialog(fontSize = fontSize, onDismiss = { showFontSizeDialog = false }, onConfirm = { newSize ->
            fontSize = newSize
            showFontSizeDialog = false
        })
    }

    if (showTextAlignDialog) {
        TextAlignDialog(textAlign = textAlign, onDismiss = { showTextAlignDialog = false }, onConfirm = { newAlign ->
            textAlign = newAlign
            showTextAlignDialog = false
        })
    }

    if (showColorDialog) {
        ColorDialog(textColor = textColor, onDismiss = { showColorDialog = false }, onConfirm = { newColor ->
            textColor = newColor
            showColorDialog = false
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFFF5EED3))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "작성할 내용을 입력해 주세요",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = fontSize,
                            textAlign = textAlign,
                            color = textColor,
                            fontFamily = when (selectedFont) {
                                "굴림체" -> FontFamily.Default
                                "궁서체" -> FontFamily.Serif
                                "바탕체" -> FontFamily.SansSerif
                                "고딕체" -> FontFamily.Monospace
                                else -> FontFamily.Default
                            }
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = fontSize,
                    textAlign = textAlign,
                    color = textColor,
                    fontFamily = when (selectedFont) {
                        "굴림체" -> FontFamily.Default
                        "궁서체" -> FontFamily.Serif
                        "바탕체" -> FontFamily.SansSerif
                        "고딕체" -> FontFamily.Monospace
                        else -> FontFamily.Default
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "익명으로 작성하기",
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = anonymous,
                    onCheckedChange = { anonymous = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (!anonymous) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "From. ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = author,
                        onValueChange = { author = it },
                        placeholder = { Text(text = "작성자") },
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fontFamilies.forEach { font ->
                    Button(
                        onClick = { selectedFont = font },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFont == font) Color.Gray else Color.LightGray
                        )
                    ) {
                        Text(text = font)
                    }
                }
            }
        }

        NavigationBar(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(R.drawable.grr), contentDescription = "Text") },
                selected = false,
                onClick = { showFontSizeDialog = true }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(R.drawable.grr), contentDescription = "Alignment") },
                selected = false,
                onClick = { showTextAlignDialog = true }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(R.drawable.grr), contentDescription = "Colors") },
                selected = false,
                onClick = { showColorDialog = true }
            )
        }
    }
}

@Composable
fun FontSizeDialog(fontSize: TextUnit, onDismiss: () -> Unit, onConfirm: (TextUnit) -> Unit) {
    var newSize by remember { mutableStateOf(fontSize) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("글씨 크기 변경") },
        text = {
            Column {
                Slider(
                    value = newSize.value,
                    onValueChange = { newSize = it.sp },
                    valueRange = 8f..32f
                )
                Text("크기: ${newSize.value.toInt()}sp")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(newSize) }) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun TextAlignDialog(textAlign: TextAlign, onDismiss: () -> Unit, onConfirm: (TextAlign) -> Unit) {
    var newAlign by remember { mutableStateOf(textAlign) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("글씨 정렬 변경") },
        text = {
            Column {
                Text("왼쪽 정렬", modifier = Modifier.clickable { newAlign = TextAlign.Start })
                Text("가운데 정렬", modifier = Modifier.clickable { newAlign = TextAlign.Center })
                Text("오른쪽 정렬", modifier = Modifier.clickable { newAlign = TextAlign.End })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(newAlign) }) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun ColorDialog(textColor: Color, onDismiss: () -> Unit, onConfirm: (Color) -> Unit) {
    var newColor by remember { mutableStateOf(textColor) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("글씨 색상 변경") },
        text = {
            Column {
                ColorPicker(initialColor = newColor, onColorChange = { newColor = it })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(newColor) }) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun ColorPicker(
    initialColor: Color,
    onColorChange: (Color) -> Unit
) {
    var hue by remember { mutableStateOf(initialColor.toHsl().hue) }
    var saturation by remember { mutableStateOf(initialColor.toHsl().saturation) }
    var lightness by remember { mutableStateOf(initialColor.toHsl().lightness) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("색상을 선택해 주세요", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Hue Slider
        Text("Hue", modifier = Modifier.padding(vertical = 8.dp))
        Slider(
            value = hue,
            onValueChange = { newHue ->
                hue = newHue
                onColorChange(Color.hsl(hue, saturation, lightness))
            },
            valueRange = 0f..360f
        )

        // Saturation Slider
        Text("Saturation", modifier = Modifier.padding(vertical = 8.dp))
        Slider(
            value =  saturation,
            onValueChange = { newSaturation ->
                saturation = newSaturation
                onColorChange(Color.hsl(hue, saturation, lightness))
            },
            valueRange = 0f..1f
        )

        // Lightness Slider
        Text("Lightness", modifier = Modifier.padding(vertical = 8.dp))
        Slider(
            value = lightness,
            onValueChange = { newLightness ->
                lightness = newLightness
                onColorChange(Color.hsl(hue, saturation, lightness))
            },
            valueRange = 0f..1f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.hsl(hue, saturation, lightness), shape = RoundedCornerShape(8.dp))
        )
    }
}

@Composable
@Preview
fun PreviewColorPicker() {
    var color by remember { mutableStateOf(Color.Red) }
    ColorPicker(initialColor = color) { newColor ->
        color = newColor
    }
}

// Extension function to convert Color to HSL
fun Color.toHsl(): HslColor {
    val r = red
    val g = green
    val b = blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    val hue = when {
        delta == 0f -> 0f
        max == r -> ((g - b) / delta) % 6
        max == g -> ((b - r) / delta) + 2
        else -> ((r - g) / delta) + 4
    } * 60f

    val lightness = (max + min) / 2
    val saturation = if (delta == 0f) 0f else delta / (1 - abs(2 * lightness - 1))

    return HslColor(hue, saturation, lightness)
}

data class HslColor(val hue: Float, val saturation: Float, val lightness: Float)

