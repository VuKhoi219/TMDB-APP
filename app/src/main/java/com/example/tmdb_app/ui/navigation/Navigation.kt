package com.example.tmdb_app.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdb_app.ui.screens.home.HomeScreen
import com.example.tmdb_app.ui.screens.home.SeeAll

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SeeAll : Screen("see_all/{title}/{api}") {
        fun createRoute(title: String, api: Int) = "see_all/$title/$api"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.SeeAll.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("api") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val api = backStackEntry.arguments?.getInt("api") ?: 0
            SeeAll(
                title = title,
                api = api,
                onClick = { navController.popBackStack() }
            )
        }
    }
}