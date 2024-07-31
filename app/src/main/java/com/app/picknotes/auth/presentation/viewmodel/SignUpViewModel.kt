package com.app.picknotes.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.app.picknotes.auth.domain.user_cases.InputValidator
import com.app.picknotes.auth.presentation.event.AuthEvent
import com.app.picknotes.auth.presentation.state.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val validator: InputValidator
): ViewModel() {
    private val _uiState = MutableStateFlow(SignUpState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: AuthEvent) {
        when(e) {
            is AuthEvent.OnUsernameChange -> TODO()
            is AuthEvent.OnEmailChange -> TODO()
            is AuthEvent.OnPasswordChange -> TODO()
            is AuthEvent.OnSubmit -> TODO()
        }
    }
}