package com.app.picknotes.utils

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)