package com.example.onetapauth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.onetapauth.presentation.screen.LoginScreen
import com.example.onetapauth.presentation.screen.ProfileScreen

@Composable
fun NavGraphSetup(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ){
        composable(route = Screen.Login.route){
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Profile.route){
            ProfileScreen(navController = navController)
        }
    }
}