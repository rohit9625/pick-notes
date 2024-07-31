package com.app.picknotes.auth.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.picknotes.auth.domain.model.InputValidationError
import com.app.picknotes.auth.domain.model.Result
import com.app.picknotes.auth.domain.user_cases.InputValidator
import com.app.picknotes.auth.presentation.event.AuthEvent
import com.app.picknotes.auth.presentation.state.SignInState
import com.app.picknotes.models.UserRequest
import com.app.picknotes.repository.UserRepository
import com.app.picknotes.utils.Constants.TAG
import com.app.picknotes.utils.NetworkResult
import com.app.picknotes.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val validator: InputValidator
): ViewModel() {
    private val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    var username by mutableStateOf("")
         private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var responseMessage by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set



    fun onEvent(e: AuthEvent) {
        when(e) {
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
                        InputValidationError.PasswordError.PASSWORD_MANDATORY -> "Enter a password"
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
                        loginUser { e.onSuccess() }
                        null
                    }
                }
                _uiState.update { it.copy(response = error) }
            }

            is AuthEvent.OnUsernameChange -> TODO("This will never happen in LoginScreen")
        }
    }

    fun signOut(onLogout: ()-> Unit) {
        try {
            tokenManager.deleteToken()
            onLogout()
        }catch (e: Exception) {
            Log.e(TAG, "Error while deleting token: $e")
        }
    }

    private fun loginUser(onSuccess: ()-> Unit) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = userRepository.loginUser(UserRequest(
                    username = username,
                    email = email,
                    password = password
                ))


                if(response is NetworkResult.Success) {
                    tokenManager.saveToken(response.data!!.token)
                    onSuccess()
                    Log.d(TAG, "Success response : ${response.data}")
                }
                else if(response is NetworkResult.Error) {
                    responseMessage = response.message!!
                    Log.e(TAG, "Error response : ${response.message}")
                }

            }catch (e: Exception) {
                Log.e(TAG, "Error : ${e.message}")
            }finally {
                isLoading = false
            }
        }
    }
}