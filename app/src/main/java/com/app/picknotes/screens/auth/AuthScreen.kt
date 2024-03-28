package com.app.picknotes.screens.auth

sealed class AuthScreen(val route: String) {
    data object SignUp : AuthScreen("sign_up")
    data object SignIn : AuthScreen("sign_in")
    data object Forgot : AuthScreen("forgot")
}