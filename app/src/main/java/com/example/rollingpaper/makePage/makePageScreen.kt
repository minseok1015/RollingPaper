package com.example.rollingpaper.makePage

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.Routes

@Composable
fun makePage(navController: NavController) {
    var selectedTheme by remember { mutableStateOf(1) }
    var titleText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("제목 입력", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = titleText,
            onValueChange = { titleText = it },
            placeholder = { Text("이름이나 단어, 문장도 가능해요.") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("테마 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

        ThemeOption("테마 1", Color(0xFFD7FBE8), selectedTheme == 1) { selectedTheme = 1 }
        ThemeOption("테마 2", Color(0xFFD7E8FB), selectedTheme == 2) { selectedTheme = 2 }
        ThemeOption("테마 3", Color(0xFFFBD7D7), selectedTheme == 3) { selectedTheme = 3 }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = { navController.navigate(Routes.Home.route) }) {
                Text("뒤로가기", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = { navController.navigate(Routes.Memo.route) }) {
                Text("페이지 생성", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ThemeOption(text: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(selected = isSelected, onClick = onClick)
    }
}