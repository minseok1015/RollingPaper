package com.example.rollingpaper

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rollingpaper.checkPage.checkPage
import com.example.rollingpaper.homePage.homeScreen
import com.example.rollingpaper.homePage.homeScreen_no
import com.example.rollingpaper.mainPage.MainPageScreen
import com.example.rollingpaper.makeMemo.makeMemoScreen
import com.google.firebase.Firebase
import com.google.firebase.database.database
import makePage

sealed class Routes(val route: String) {
    object Home : Routes("Home")
    object Page : Routes("Page/{pageId}?title={title}&theme={theme}")
    object Memo : Routes("Memo/{pageId}?title={title}&theme={theme}")
    object MakePage : Routes("MakePage")
    object EnterPage : Routes("EnterPage")
}

@Composable
fun Graph(navController: NavHostController, kakaoAuthViewModel: KakaoAuthViewModel = viewModel()) {
    val isLoggedIn by kakaoAuthViewModel.isLoggedIn.observeAsState(initial = false)
    val loginEvent by kakaoAuthViewModel.loginEvent.observeAsState()
    val table = Firebase.database.getReference("Pages")
    val application = LocalContext.current.applicationContext as Application
    val memoModel: MemoViewModel = viewModel(factory = MemoViewModelFactory(application, Repository(table)))
    val stickerViewModel: StickerViewModel = viewModel()

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

        composable(route = Routes.Page.route) { backStackEntry ->
            val pageId = backStackEntry.arguments?.getString("pageId")
            val title = backStackEntry.arguments?.getString("title")
            val theme = backStackEntry.arguments?.getString("theme")?.toIntOrNull()
            MainPageScreen(pageId = pageId, title = title, theme = theme, navController = navController, memoModel = memoModel, kakaoAuthViewModel = kakaoAuthViewModel)
        }

        composable(route = Routes.Memo.route) { backStackEntry ->
            val pageId = backStackEntry.arguments?.getString("pageId")
            val title = backStackEntry.arguments?.getString("title")
            val theme = backStackEntry.arguments?.getString("theme")?.toIntOrNull()
            println("Received pageId: $pageId, title: $title, theme: $theme")
            pageId?.let {
                makeMemoScreen(pageId = it,  navController = navController, memoModel = memoModel, kakaoAuthViewModel = kakaoAuthViewModel)
            }
        }

        composable(route = Routes.MakePage.route) {
            makePage(navController)
        }

        composable(route = Routes.EnterPage.route) {
            checkPage(navController, memoModel)
        }
    }
}
