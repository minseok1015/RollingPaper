package com.example.rollingpaper.EnterPage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.MemoViewModel

@Composable
fun EnterPageScreen(navController: NavController, memoViewModel: MemoViewModel) {
    val inputText = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "입장하기",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BasicTextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val pageId = inputText.value
                memoViewModel.getPageInfo(pageId, onSuccess = { page ->
                    navController.navigate("Page/${page.pageId}?title=${page.title}&theme=${page.theme}")
                }, onError = {
                    Toast.makeText(context, "Page not found", Toast.LENGTH_SHORT).show()
                })
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth().size(50.dp)
        ) {
            Text("확인하기", color = Color.White)
        }
    }
}
