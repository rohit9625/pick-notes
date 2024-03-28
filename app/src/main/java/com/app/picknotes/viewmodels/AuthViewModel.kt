package com.app.picknotes.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.picknotes.models.UserRequest
import com.app.picknotes.repository.UserRepository
import com.app.picknotes.utils.Constants.TAG
import com.app.picknotes.utils.NetworkResult
import com.app.picknotes.utils.TextFieldErrors
import com.app.picknotes.utils.TokenManager
import com.app.picknotes.utils.Validation
import com.app.picknotes.utils.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val validation: Validation
): ViewModel() {

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


    @OptIn(ExperimentalCoroutinesApi::class)
    var fieldError: StateFlow<TextFieldErrors> = snapshotFlow { Triple(username, email, password) }
        .filter { (username, email, password) ->
            username.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty()
        }
        .mapLatest { (username, email, password) ->
            TextFieldErrors(
                usernameError = if (username.isNotEmpty()) validation.validateUsername(username) else ValidationResult(successful = true),
                emailError = if (email.isNotEmpty()) validation.validateEmail(email) else ValidationResult(successful = true),
                passwordError = if (password.isNotEmpty()) validation.validatePassword(password) else ValidationResult(successful = true)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TextFieldErrors()
        )

    fun signUp(onSuccess: ()-> Unit) {
        val blankValidation = validation.validateBlankCheck(username, email, password)
        if(!blankValidation.successful){
            responseMessage = blankValidation.errorMessage!!
            isLoading = false
            return
        }
        responseMessage = ""
        viewModelScope.launch {

            try {
                isLoading = true
                val response = userRepository.registerUser(UserRequest(
                    username = username,
                    email = email,
                    password = password
                ))


                if(response is NetworkResult.Success) {
                    tokenManager.saveToken(response.data!!.token)
                    onSuccess()
                    Log.d(TAG, "Success response : ${response.data}, Token: ${tokenManager.getToken()}")
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

    fun signOut(onLogout: ()-> Unit) {
        try {
            tokenManager.deleteToken()
            onLogout()
        }catch (e: Exception) {
            Log.e(TAG, "Error while deleting token: $e")
        }
    }

    fun signIn(onSuccess: ()-> Unit) {
        val blankValidation = validation.validateBlankCheck(email, password)
        if(!blankValidation.successful){
            responseMessage = blankValidation.errorMessage!!
            isLoading = false
            return
        }
        responseMessage = ""
        viewModelScope.launch {

            try {
                isLoading = true
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

    fun updateUsername(input : String) {
        username = input
    }
    fun updateEmail(input : String) {
        email = input
    }
    fun updatePassword(input : String) {
        password = input
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as NotesApplication)
//                val notesRepository = application.container.notesRepository
//                val userRepository = application.container.userRepository
//                val tokenManager = application.container.tokenManager
//                val validation = application.container.validation
//
//                AuthViewModel(
//                    userRepository = userRepository,
//                    tokenManager = tokenManager,
//                    validation = validation
//                )
//            }
//        }
//    }
}