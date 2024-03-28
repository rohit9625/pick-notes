package com.app.picknotes.repository

import com.app.picknotes.models.UserRequest
import com.app.picknotes.models.UserResponse
import com.app.picknotes.network.UserApi
import com.app.picknotes.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    suspend fun registerUser(userRequest: UserRequest) : NetworkResult<UserResponse>{
        return try {
            val response = userApi.register(userRequest)
            if(response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            }else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = JSONObject(errorBody).getString("message")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun loginUser(userRequest: UserRequest): NetworkResult<UserResponse> {
        return try {
            val response = userApi.login(userRequest)

            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = JSONObject(errorBody).getString("message")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}