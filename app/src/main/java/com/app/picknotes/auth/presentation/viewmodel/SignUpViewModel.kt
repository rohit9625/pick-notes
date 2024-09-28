package com.app.picknotes.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.picknotes.auth.data.UserRepository
import com.app.picknotes.auth.domain.model.InputValidationError
import com.app.picknotes.auth.domain.model.NetworkError
import com.app.picknotes.auth.domain.model.Result
import com.app.picknotes.auth.domain.user_cases.InputValidator
import com.app.picknotes.auth.presentation.event.AuthEvent
import com.app.picknotes.auth.presentation.state.SignUpState
import com.app.picknotes.auth.presentation.state.toSignUp
import com.app.picknotes.utils.Constants
import com.app.picknotes.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validator: InputValidator,
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SignUpState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: AuthEvent) {
        when(e) {
            is AuthEvent.OnUsernameChange -> {
                val error = when(val res = validator.validateUsername(e.username)) {
                    is Result.Error -> when(res.error) {
                        InputValidationError.UsernameError.USERNAME_MANDATORY -> "Enter your username"
                        InputValidationError.UsernameError.WHITESPACE_NOT_ALLOWED -> "Whitespaces not allowed"
                        InputValidationError.UsernameError.USERNAME_TOO_SHORT -> "Username is too short"
                        InputValidationError.UsernameError.USERNAME_TOO_LONG -> "Username is too long"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(username = e.username, usernameError = error) }
            }
            is AuthEvent.OnEmailChange -> {
                val error = when(val res = validator.validateEmail(e.email)) {
                    is Result.Error -> when(res.error) {
                        InputValidationError.EmailError.EMAIL_MANDATORY -> "Enter your email"
                        InputValidationError.EmailError.EMAIL_INVALID -> "Email is invalid"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(email = e.email, emailError = error) }
            }
            is AuthEvent.OnPasswordChange -> {
                val error = when(val res = validator.validatePassword(e.password)) {
                    is Result.Error -> when(res.error) {
                        InputValidationError.PasswordError.PASSWORD_MANDATORY -> "Enter your password"
                        InputValidationError.PasswordError.WHITESPACE_NOT_ALLOWED -> "Whitespace not allowed"
                        InputValidationError.PasswordError.PASSWORD_LENGTH -> "Length should be 8-16 characters"
                        InputValidationError.PasswordError.MISSING_ALPHABET -> "Must contain at least 1 alphabet"
                        InputValidationError.PasswordError.MISSING_NUMBER -> "Must contain at least 1 number"
                        InputValidationError.PasswordError.MISSING_SYMBOL -> "Must contain at least 1 symbol (@,#,$)"
                        InputValidationError.PasswordError.PASSWORD_NOT_MATCHED -> null
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(password = e.password, passwordError = error) }
            }
            is AuthEvent.OnSubmit -> {
                _uiState.update { it.copy(isLoading = true) }
                val error = when(
                    val res = validator.validateBlankCheck(
                        email = uiState.value.email,
                        password = uiState.value.password
                    )
                ) {
                    is Result.Error -> when(res.error) {
                        InputValidationError.GeneralError.MANDATORY -> "All fields are mandatory"
                        InputValidationError.GeneralError.INVALID -> null
                    }
                    is Result.Success -> {
                        signUpUser(e.onSuccess)
                        null
                    }
                }
                _uiState.update { it.copy(response = error, isLoading = false) }
            }
        }
    }

    private fun signUpUser(onSuccess: ()-> Unit) {
        viewModelScope.launch {
            try {
                val response = when(val res = userRepository.registerUser(_uiState.value.toSignUp())) {
                    is Result.Error -> when(res.error) {
                        NetworkError.UserError.USER_ALREADY_EXISTS -> "User already exists"
                        else -> "Please try again later"
                    }
                    is Result.Success -> {
                        tokenManager.saveToken(res.data.token)
                        onSuccess()
                        null
                    }
                }
                _uiState.update { it.copy(isLoading = false, response = response) }
            } catch(e: Exception) {
                Log.e(Constants.TAG, "Error occurred while signing-up user", e)
                _uiState.update { it.copy(isLoading = false, response = "An unexpected error occurred") }
            }
        }
    }
}