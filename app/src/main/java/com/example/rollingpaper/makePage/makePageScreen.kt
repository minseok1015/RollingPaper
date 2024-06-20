package com.example.rollingpaper.checkPage

import PageViewModel
import PageViewModelFactory
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rollingpaper.Page
import com.example.rollingpaper.R
import com.example.rollingpaper.Repository
import com.example.rollingpaper.Routes
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.UUID

@Composable
fun makePage(navController: NavController,pageViewModel: PageViewModel = viewModel(factory = PageViewModelFactory(
    Repository(Firebase.database.getReference("/Pages"))
))) {
    var selectedTheme by remember { mutableStateOf(1) }
    var titleText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EED3))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("제목 입력", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = titleText,
            onValueChange = { titleText = it },
            placeholder = { Text("이름이나 단어, 문장도 가능해요.") },
            modifier = Modifier.fillMaxWidth(0.8f),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("테마 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

        ThemeOption("테마 1", painterResource(R.drawable.theme1), selectedTheme == 1) { selectedTheme = 1 }
        ThemeOption("테마 2", painterResource(R.drawable.theme2), selectedTheme == 2) { selectedTheme = 2 }
        ThemeOption("테마 3", painterResource(R.drawable.theme3), selectedTheme == 3) { selectedTheme = 3 }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate(Routes.Home.route) },
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
                    val pageId = generatePageId()
                    val newPage = Page(pageId = pageId, theme = selectedTheme, title = titleText)
                    pageViewModel.insertPage(newPage)
                    navController.navigate("Page/${pageId}?title=${titleText}&theme=${selectedTheme}")
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("페이지 생성", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun ThemeOption(text: String, painter: Painter, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Transparent, shape = CircleShape)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(selected = isSelected, onClick = onClick)
    }
}

fun generatePageId(): String {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 10)
}
