package com.example.rollingpaper.makeMemo

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.MainActivity
import com.example.rollingpaper.R

@Composable
fun makeMemoScreen(navController: NavController,kakaoAuthViewModel: KakaoAuthViewModel) {


    val context = kakaoAuthViewModel.context

    var text by remember { mutableStateOf("") }
    var anonymous by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var showTextAlignDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16.sp) }
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
            .background(Color(0xFFF5EED3))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color(0xFFF5EED3), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                textStyle = TextStyle(
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
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = when (textAlign) {
                            TextAlign.Center -> Alignment.Center
                            TextAlign.End -> Alignment.CenterEnd
                            else -> Alignment.CenterStart
                        }
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "작성할 내용을 입력해 주세요",
                                style = TextStyle(
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
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        innerTextField()
                    }
                }
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
                    ,color = Color.Black
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
                        ,color = Color.Black
                    )
                    BasicTextField(
                        value = author,
                        onValueChange = { author = it },
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        textStyle = TextStyle(
                            fontSize = fontSize,
                            color = textColor,
                            fontFamily = when (selectedFont) {
                                "굴림체" -> FontFamily.Default
                                "궁서체" -> FontFamily.Serif
                                "바탕체" -> FontFamily.SansSerif
                                "고딕체" -> FontFamily.Monospace
                                else -> FontFamily.Default
                            }
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (author.isEmpty()) {
                                    Text(
                                        text = "작성자",
                                        style = TextStyle(
                                            fontSize = fontSize,
                                            color = textColor,
                                            fontFamily = when (selectedFont) {
                                                "굴림체" -> FontFamily.Default
                                                "궁서체" -> FontFamily.Serif
                                                "바탕체" -> FontFamily.SansSerif
                                                "고딕체" -> FontFamily.Monospace
                                                else -> FontFamily.Default
                                            }
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                                innerTextField()
                            }
                        }
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
        Button(onClick = { kakaoAuthViewModel.shareContent(
            context = context
            ,"Dd"
            ,""
            ,""
            ,"https://example.com/callback/"
        ) },
            modifier = Modifier.fillMaxWidth()
                , colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                    )
        ) {
            Text("확인", color = Color.White)
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
fun TextAlignDialog(
    textAlign: TextAlign,
    onDismiss: () -> Unit,
    onConfirm: (TextAlign) -> Unit
) {
    var newAlign by remember { mutableStateOf(textAlign) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("글씨 정렬 변경") },
        text = {
            Column {
                Button(
                    onClick = { newAlign = TextAlign.Start },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("왼쪽 정렬")
                }
                Button(
                    onClick = { newAlign = TextAlign.Center },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("가운데 정렬")
                }
                Button(
                    onClick = { newAlign = TextAlign.End },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("오른쪽 정렬")
                }
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val colors = listOf(Color.Red, Color.Magenta, Color.Yellow, Color.Green, Color.Blue, Color.Black)
                    colors.forEach { color ->
                        Button(
                            onClick = { newColor = color },
                            colors = ButtonDefaults.buttonColors(containerColor = color),
                            modifier = Modifier.size(40.dp)
                        ) {}
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun MakeMemoScreenPreview() {
    val navController = rememberNavController()
//    makeMemoScreen(navController)
}