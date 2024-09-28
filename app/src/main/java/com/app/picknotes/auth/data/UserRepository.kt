package com.app.picknotes.auth.data

import android.util.Log
import com.app.picknotes.auth.data.model.SignInRequest
import com.app.picknotes.auth.data.model.SignUpRequest
import com.app.picknotes.auth.data.model.Token
import com.app.picknotes.auth.domain.model.NetworkError
import com.app.picknotes.auth.domain.model.Result
import com.app.picknotes.auth.data.remote.UserApi
import com.app.picknotes.utils.Constants
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    suspend fun registerUser(user: SignUpRequest) : Result<Token, NetworkError>{
        return try {
            val response = userApi.register(user)

            if(response.code() == 201) {
                Result.Success(response.body()!!.data)
            } else if(response.code() == 409) {
                Result.Error(NetworkError.UserError.USER_ALREADY_EXISTS)
            } else {
                Result.Error(NetworkError.UserError.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error registering new user", e)
            Result.Error(NetworkError.UserError.UNKNOWN_ERROR)
        }
    }

    suspend fun loginUser(user: SignInRequest): Result<Token, NetworkError> {
        return try {
            val response = userApi.login(user)

            if (response.code() == 200) {
                Result.Success(response.body()!!.data)
            } else if(response.code() == 404) {
                Result.Error(NetworkError.UserError.USER_NOT_EXISTS)
            } else if(response.code() == 401) {
                Result.Error(NetworkError.UserError.INVALID_CREDENTIALS)
            } else {
                Result.Error(NetworkError.UserError.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error while login user", e)
            Result.Error(NetworkError.UserError.UNKNOWN_ERROR)
        }
    }
}