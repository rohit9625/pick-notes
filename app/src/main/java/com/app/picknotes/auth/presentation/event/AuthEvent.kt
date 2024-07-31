package com.app.picknotes.auth.presentation.event

sealed interface AuthEvent {
    data class OnUsernameChange(val username: String): AuthEvent
    data class OnEmailChange(val email: String): AuthEvent
    data class OnPasswordChange(val password: String): AuthEvent
    data class OnSubmit(val onSuccess: ()-> Unit): AuthEvent
}