package com.app.picknotes.utils

data class TextFieldErrors(
    val usernameError: ValidationResult = ValidationResult(successful = true),
    val emailError: ValidationResult = ValidationResult(successful = true),
    val passwordError: ValidationResult = ValidationResult(successful = true)
)