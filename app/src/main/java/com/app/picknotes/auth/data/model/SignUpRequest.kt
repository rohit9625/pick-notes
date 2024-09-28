package com.app.picknotes.auth.data.model

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)
