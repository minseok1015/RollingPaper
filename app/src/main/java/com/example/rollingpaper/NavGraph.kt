package com.example.rollingpaper

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rollingpaper.homePage.homeScreen
import com.example.rollingpaper.mainPage.MainPageScreen
import com.example.rollingpaper.makeMemo.makeMemoScreen
import com.example.rollingpaper.makePage.makePage
import com.google.firebase.Firebase
import com.google.firebase.database.database

sealed class Routes(val route:String){
    object Home:Routes("Home");
    object Page:Routes("Page");
    object Memo:Routes("Memo");
    object MakePage:Routes("MakePage");
}


@Composable
fun Graph(navController: NavHostController){
    val table= Firebase.database.getReference("Pages/memos")
    val application = LocalContext.current.applicationContext as Application
    val memoModel: MemoViewModel = viewModel(factory = MemoViewModelFactory(application, Repository(table)))

    NavHost(navController = navController, startDestination = Routes.Home.route){
        composable(route=Routes.Home.route){
            homeScreen(navController)
        }
        composable(route=Routes.Page.route)
        {
            MainPageScreen(navController,memoModel)
        }
        composable(route=Routes.Memo.route)
        {
            makeMemoScreen(navController,memoModel)
        }
        composable(route=Routes.MakePage.route)
        {
            makePage(navController)
        }

    }
}