package com.example.rollingpaper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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
                    val navcontroller= rememberNavController()
                    Graph(navcontroller)
//
                        // 다른 destination 추가
                    }
                }
            }
        }
    }



