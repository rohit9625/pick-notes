package com.app.picknotes.auth.data.remote

import com.app.picknotes.models.UserRequest
import com.app.picknotes.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/users/signup")
    suspend fun register(@Body userRequest: UserRequest) : Response<UserResponse>

    @POST("/users/signin")
    suspend fun login(@Body userRequest: UserRequest) : Response<UserResponse>
}