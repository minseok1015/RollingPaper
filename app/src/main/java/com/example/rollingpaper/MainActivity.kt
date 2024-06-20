package com.example.rollingpaper

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.rollingpaper.ui.theme.RollingPaperTheme
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.kakao.sdk.common.util.Utility


class MainActivity : ComponentActivity() {

    private val kakaoAuthViewModel: KakaoAuthViewModel by viewModels()
//    val table= Firebase.database.getReference("Pages")
//    val application = LocalContext.current.applicationContext as Application
//    val memoModel: MemoViewModel = viewModel(factory = MemoViewModelFactory(application, Repository(table)))
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
//                    // NavHost에 NavController 연결
//                    NavHost(navController, startDestination = "homeScreen") {
//                        composable("homeScreen") {
//                            homeScreen_no(navController, viewModel = kakaoAuthViewModel)
//                        }
//                        composable("makeMemoScreen") {
//                            makeMemoScreen(navController)
//                        }
//                    val isLoggedIn by kakaoAuthViewModel.isLoggedIn.observeAsState(false)
//                    if(isLoggedIn) {
//                        homeScreen(navController)
//                    } else {
//                        homeScreen_no(viewModel = kakaoAuthViewModel)
//                    }

                    val keyHash = Utility.getKeyHash(this)
                    Log.d("Hash", keyHash)
                    kakaoAuthViewModel.checkLoginStatus()
                    val navcontroller= rememberNavController()
                    Graph(navcontroller, kakaoAuthViewModel)

                        // 다른 destination 추가
                    }
                }
            }
        }

    }


