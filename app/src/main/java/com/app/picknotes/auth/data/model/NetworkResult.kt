package com.app.picknotes.auth.data.model

data class NetworkResult(
    val success: Boolean,
    val data: Token,
    val message: String
)

data class Token(
    val token: String
)