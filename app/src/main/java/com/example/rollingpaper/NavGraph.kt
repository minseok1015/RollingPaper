package com.example.rollingpaper

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rollingpaper.homePage.homeScreen
import com.example.rollingpaper.mainPage.MainPageScreen
import com.example.rollingpaper.makeMemo.makeMemoScreen
import com.example.rollingpaper.makePage.makePage

sealed class Routes(val route:String){
    object Home:Routes("Home");
    object Page:Routes("Page");
    object Memo:Routes("Memo");
    object MakePage:Routes("MakePage");
}


@Composable
fun Graph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Routes.Home.route){
        composable(route=Routes.Home.route){
            homeScreen(navController)
        }
        composable(route=Routes.Page.route)
        {
            MainPageScreen(navController)
        }
        composable(route=Routes.Memo.route)
        {
            makeMemoScreen(navController)
        }
        composable(route=Routes.MakePage.route)
        {
            makePage(navController)
        }

    }
}