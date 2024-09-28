package com.app.picknotes.auth.data.remote

import com.app.picknotes.auth.data.model.NetworkResult
import com.app.picknotes.auth.data.model.SignInRequest
import com.app.picknotes.auth.data.model.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/users/register")
    suspend fun register(@Body user: SignUpRequest) : Response<NetworkResult>

    @POST("/users/login")
    suspend fun login(@Body user: SignInRequest) : Response<NetworkResult>
}