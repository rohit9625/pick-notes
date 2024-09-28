package com.app.picknotes.auth.presentation.state

import com.app.picknotes.auth.data.model.SignUpRequest

data class SignUpState(
    val username: String = "",
    val usernameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val response: String? = null,
    val isLoading: Boolean = false
) {
    val hasError: Boolean
        get() = usernameError != null || emailError != null || passwordError != null
}
fun SignUpState.toSignUp() = SignUpRequest(username = username, email = email, password = password)
