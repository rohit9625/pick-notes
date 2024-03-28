package com.app.picknotes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.picknotes.Graph
import com.app.picknotes.screens.auth.AuthScreen
import com.app.picknotes.screens.auth.SignInScreen
import com.app.picknotes.screens.auth.SignUpScreen

fun NavGraphBuilder.authNavigation(navController: NavController) {
    navigation(route = Graph.AUTH, startDestination = AuthScreen.SignIn.route) {
        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(
                navController = navController,
            )
        }
        composable(route = AuthScreen.SignIn.route) {
            SignInScreen(
                navController = navController,
            )
        }
        composable(route = AuthScreen.Forgot.route) {

        }
    }
}

