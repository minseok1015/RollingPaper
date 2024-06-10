package com.example.rollingpaper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rollingpaper.homePage.homeScreen
import com.example.rollingpaper.homePage.homeScreen_no
import com.example.rollingpaper.mainPage.MainPageScreen
import com.example.rollingpaper.makeMemo.makeMemoScreen
import com.example.rollingpaper.makePage.makePage

sealed class Routes(val route: String) {
    object Home : Routes("Home")
    object Page : Routes("Page")
    object Memo : Routes("Memo")
    object MakePage : Routes("MakePage")
}

@Composable
fun Graph(navController: NavHostController, kakaoAuthViewModel: KakaoAuthViewModel = viewModel()) {
    val isLoggedIn by kakaoAuthViewModel.isLoggedIn.observeAsState(initial = false)
    val loginEvent by kakaoAuthViewModel.loginEvent.observeAsState()

    loginEvent?.getContentIfNotHandled()?.let {
        if (it) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Home.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(route = Routes.Home.route) {
            if (isLoggedIn) {
                homeScreen(navController, kakaoAuthViewModel)
            } else {
                homeScreen_no(viewModel = kakaoAuthViewModel, navController = navController)
            }
        }
        composable(route = Routes.Page.route) {
            MainPageScreen(navController)
        }
        composable(route = Routes.Memo.route) {
            makeMemoScreen(navController,kakaoAuthViewModel)
        }
        composable(route = Routes.MakePage.route) {
            makePage(navController)
        }
    }
}
