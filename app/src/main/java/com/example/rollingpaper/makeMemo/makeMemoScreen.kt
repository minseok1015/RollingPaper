package com.example.rollingpaper.makeMemo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.Memo
import com.example.rollingpaper.MemoViewModel
import com.example.rollingpaper.R
@Composable
fun makeMemoScreen(pageId: String, navController: NavController, kakaoAuthViewModel: KakaoAuthViewModel, memoModel: MemoViewModel) {

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
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
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
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
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

        val memo = Memo(1,"데이터베이스 삽입테스트","민석",14,1,2,14,14,1)
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
        Button(onClick = {
//            kakaoAuthViewModel.shareContent(
//            context = context
//            ,"Dd"
//            ,""
//            ,""
//            ,"https://example.com/callback/"
//
//        )

            memoModel.insertMemo(pageId,Memo(2,"데이터베이스 삽입테스트","민석",14,1,2,14,14,1))
            memoModel.getPageInfo(pageId, onSuccess = { page ->
                navController.navigate("Page/${page.pageId}?title=${page.title}&theme=${page.theme}")
            }, onError = {
                Toast.makeText(context, "해당 페이지를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
            })


                         },
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
    var sliderPosition by remember { mutableStateOf(fontSize.value) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "글씨 크기 설정") },
        text = {
            Column {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 8f..48f,
                    steps = 40,
                    onValueChangeFinished = { }
                )
                Text(text = "크기: ${sliderPosition.toInt()}")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(sliderPosition.sp) }) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "취소")
            }
        }
    )
}

@Composable
fun TextAlignDialog(textAlign: TextAlign, onDismiss: () -> Unit, onConfirm: (TextAlign) -> Unit) {
    var selectedTextAlign by remember { mutableStateOf(textAlign) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "텍스트 정렬 설정") },
        text = {
            Column {
                listOf(TextAlign.Start, TextAlign.Center, TextAlign.End).forEach { align ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = if (selectedTextAlign == align) Color.Gray else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = selectedTextAlign == align,
                            onClick = { selectedTextAlign = align }
                        )
                        Text(
                            text = align.name,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedTextAlign) }) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "취소")
            }
        }
    )
}

@Composable
fun ColorDialog(textColor: Color, onDismiss: () -> Unit, onConfirm: (Color) -> Unit) {
    var selectedColor by remember { mutableStateOf(textColor) }
    val colors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Gray)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "글씨 색상 설정") },
        text = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == color) Color.Black else Color.Transparent,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .background(color = color)
                                .padding(4.dp)
                                .background(color = color, shape = RoundedCornerShape(4.dp))
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedColor) }) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "취소")
            }
        }
    )
}
