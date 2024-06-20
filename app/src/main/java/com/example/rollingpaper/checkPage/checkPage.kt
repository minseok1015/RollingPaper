package com.example.rollingpaper.checkPage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rollingpaper.MemoViewModel

@Composable
fun checkPage(navController: NavController, memoViewModel: MemoViewModel = viewModel()) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EED3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "입장하기",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("코드를 입력하세요.") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
        )

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text(text = "뒤로가기", color = Color.White, fontSize = 18.sp)
            }

            Button(
                onClick = {
                    val pageId = text
                    if (pageId.isEmpty()) {
                        Toast.makeText(context, "Page ID를 입력하세요", Toast.LENGTH_SHORT).show()
                    } else {
                        memoViewModel.getPageInfo(pageId, onSuccess = { page ->
                            navController.navigate("Page/${page.pageId}?title=${page.title}&theme=${page.theme}")
                        }, onError = {
                            Toast.makeText(context, "해당 페이지를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                        })
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text(text = "확인하기", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
