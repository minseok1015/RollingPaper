package com.example.rollingpaper

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rollingpaper.mainPage.MainPageScreen
import com.example.rollingpaper.homePage.homeScreen
import com.example.rollingpaper.homePage.homeScreen_no
import com.example.rollingpaper.makeMemo.makeMemoScreen
import com.example.rollingpaper.makePage.makePage
import com.example.rollingpaper.ui.theme.RollingPaperTheme


class MainActivity : ComponentActivity() {
    private val kakaoAuthViewModel: KakaoAuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RollingPaperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavController 초기화
                    val navController = rememberNavController()

//                    // NavHost에 NavController 연결
//                    NavHost(navController, startDestination = "homeScreen") {
//                        composable("homeScreen") {
//                            homeScreen_no(navController, viewModel = kakaoAuthViewModel)
//                        }
//                        composable("makeMemoScreen") {
//                            makeMemoScreen()
//                        }
                    homeScreen_no(viewModel = kakaoAuthViewModel)
//
                        // 다른 destination 추가
                    }
                }
            }
        }
    }


