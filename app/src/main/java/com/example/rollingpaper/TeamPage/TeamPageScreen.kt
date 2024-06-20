package com.example.rollingpaper.TeamPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.R

@Composable
fun TeamPageScreen(navController: NavController) {
    val backgroundPainter: Painter = painterResource(id = R.drawable.background)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color(0xFF3E2723),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { navController.navigateUp() }
            )
            Spacer(modifier = Modifier.height(24.dp))
            TeamMember(name = "김민석", year = "3학년", major = "컴퓨터공학과")
            Spacer(modifier = Modifier.height(16.dp))
            TeamMember(name = "정재우", year = "4학년", major = "컴퓨터공학과")
            Spacer(modifier = Modifier.height(16.dp))
            TeamMember(name = "남경식", year = "3학년", major = "컴퓨터공학과")
            Spacer(modifier = Modifier.height(16.dp))
            TeamMember(name = "이정빈", year = "3학년", major = "컴퓨터공학과")
        }
    }
}

@Composable
fun TeamMember(name: String, year: String, major: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E2723)
        )
        Text(
            text = year,
            fontSize = 20.sp,
            color = Color(0xFF3E2723)
        )
        Text(
            text = major,
            fontSize = 20.sp,
            color = Color(0xFF3E2723)
        )
    }
}
