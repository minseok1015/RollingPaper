package com.example.rollingpaper.makeMemo

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import com.example.rollingpaper.component.Colors
import com.example.rollingpaper.component.FontColors
import com.example.rollingpaper.component.Fonts

@Composable
fun makeMemoScreen(pageId: String, navController: NavController, kakaoAuthViewModel: KakaoAuthViewModel, memoModel: MemoViewModel) {

    val context = kakaoAuthViewModel.context

    var text by remember { mutableStateOf("") }
    var anonymous by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var showTextAlignDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }
    var showBackgroundColorDialog by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16.sp) }
    var textAlign by remember { mutableStateOf(TextAlign.Start) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var author by remember { mutableStateOf("") }

    var selectedFontIndex by remember { mutableStateOf(0) }

    if (showFontSizeDialog) {
        FontSizeDialog(fontSize = fontSize, onDismiss = { showFontSizeDialog = false }, onConfirm = { newSize ->
            fontSize = newSize
            showFontSizeDialog = false
        })
    }



    if (showColorDialog) {
        ColorDialog(textColor, onDismiss = { showColorDialog = false }, onConfirm = { newColor ->
            textColor = newColor
            showColorDialog = false
        })
    }

    if (showBackgroundColorDialog) {
        BackgroundColorDialog(backgroundColor, onDismiss = { showBackgroundColorDialog = false }, onConfirm = { newColor ->
            backgroundColor = newColor
            showBackgroundColorDialog = false
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
                    fontFamily = Fonts.getFontByIndex(selectedFontIndex)
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
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
                                    fontFamily = Fonts.getFontByIndex(selectedFontIndex)
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
                            fontFamily = Fonts.getFontByIndex(selectedFontIndex)
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
                                            fontFamily = Fonts.getFontByIndex(selectedFontIndex)
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
                Fonts.fontArray.forEachIndexed { index, font ->
                    Button(
                        onClick = { selectedFontIndex = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFontIndex == index) Color.Gray else Color.LightGray
                        )
                    ) {
                        Text(text = font.name, fontFamily = font.font)
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))

            NavigationBar(
                containerColor = Color.LightGray,
                contentColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)

            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Text Size") },
                    selected = false,
                    onClick = { showFontSizeDialog = true }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Text Color") },
                    selected = false,
                    onClick = { showColorDialog = true }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(R.drawable.bono1), contentDescription = "Background") },
                    selected = false,
                    onClick = { showBackgroundColorDialog = true }
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("뒤로가기", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Button(
                onClick = {
                    val newMemoId = memoModel.getNextMemoId()
                    memoModel.insertMemo(
                        pageId,
                        Memo(
                            memoId = newMemoId,
                            content = text,
                            name = if (anonymous) "익명" else author,
                            font = selectedFontIndex,
                            fontSize = fontSize.value.toInt(),
                            fontColor = FontColors.fontColorsArray.indexOfFirst { it.color == textColor },
                            memoColor = Colors.colorsArray.indexOfFirst { it.color == backgroundColor },
                            like = 0,

                            )
                    )
                    memoModel.getPageInfo(pageId, onSuccess = { page ->
                        navController.navigate("Page/${page.pageId}?title=${page.title}&theme=${page.theme}")
                    }, onError = {
                        Toast.makeText(context, "해당 페이지를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                    })
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("메모 만들기", fontWeight = FontWeight.Bold, color = Color.White)
            }
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
                    val colors = FontColors.fontColorsArray
                    colors.forEachIndexed { index, colorObj ->
                        Button(
                            onClick = { newColor = colorObj.color },
                            colors = ButtonDefaults.buttonColors(containerColor = colorObj.color),
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

@Composable
fun BackgroundColorDialog(backgroundColor: Color, onDismiss: () -> Unit, onConfirm: (Color) -> Unit) {
    var newColor by remember { mutableStateOf(backgroundColor) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("배경 색상 변경") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val colors = Colors.colorsArray
                    colors.forEachIndexed { index, colorObj ->
                        Button(
                            onClick = { newColor = colorObj.color },
                            colors = ButtonDefaults.buttonColors(containerColor = colorObj.color),
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
