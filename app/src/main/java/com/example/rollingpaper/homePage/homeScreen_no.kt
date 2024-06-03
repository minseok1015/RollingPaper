package com.example.rollingpaper.homePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.Routes


@Composable
fun homeScreen_no(viewModel : KakaoAuthViewModel, navController: NavController) {
//    navController: NavController,
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
                onClick = { },
//                navController.navigate("mainPageScreen")
                        colors = ButtonDefaults.buttonColors( Color(0xFF3C352E)),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "입장하기", color = Color.White, fontSize = 18.sp)
            }

            Button(
                onClick = { viewModel.handleKakaoLogin{ user ->
                    user?.let {
                        navController.navigate(Routes.Page.route)
                    }
                } },
                colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "로그인", color = Color.White, fontSize = 18.sp)
            }

            }
        }
    }

