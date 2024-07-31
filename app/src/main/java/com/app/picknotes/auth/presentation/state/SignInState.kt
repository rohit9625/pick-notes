package com.app.picknotes.auth.presentation.state

data class SignInState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val response: String? = null,
    val isLoading: Boolean = false
) {
    val hasError: Boolean
        get() = emailError != null || passwordError != null
}
