package com.example.rollingpaper.homePage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.rollingpaper.KakaoAuthViewModel



@Composable
fun homeScreen_no(navController: NavController, viewModel: KakaoAuthViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(initial = false)
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("Home") {
                popUpTo("Home") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EED3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Rolling Paper",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            Button(
                onClick = { /* 입장하기 버튼 클릭 시 동작 */ },
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "입장하기", color = Color.White, fontSize = 18.sp)
            }

            Button(
                onClick = { viewModel.handleKakaoLogin() },
                colors = ButtonDefaults.buttonColors(Color(0xFFFEE500)),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(60.dp)
                    .padding(vertical = 8.dp)
                ,
                    shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(id =com.example.rollingpaper.R.drawable.kakao_login_medium_narrow ),
                   contentScale = ContentScale.Crop,
                    contentDescription = "Localized description",
                    modifier = Modifier.fillMaxHeight()
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    val viewModel: KakaoAuthViewModel = viewModel()
    homeScreen_no(navController, viewModel)
}
