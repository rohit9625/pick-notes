package com.app.picknotes.utils

import android.util.Patterns
import javax.inject.Inject

class Validation @Inject constructor() {

    fun validateBlankCheck(username: String, email: String, password: String):ValidationResult {
        if(username.isBlank() ||  email.isBlank() || password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "All fields are required"
            )
        }

        return ValidationResult(successful = true)
    }
    fun validateBlankCheck(email: String, password: String):ValidationResult {
        if(email.isBlank() || password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "All fields are required"
            )
        }

        return ValidationResult(successful = true)
    }
    /**
     * This method checks for the username if it's empty, contain space or any special characters and for length
     *  {Input} - Username string
     *  {output} - ValidationResult object
     */
    fun validateUsername(username: String): ValidationResult {
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Username cannot be empty"
            )
        }

        if (username.length > 16) {
            return ValidationResult(
                successful = false,
                errorMessage = "Username length must be at most 16 characters"
            )
        }

        if (!username.all { it.isLetterOrDigit() }) {
            return ValidationResult(
                successful = false,
                errorMessage = "Username can only contain letters and digits"
            )
        }

        return ValidationResult(successful = true)
    }
    /**
     * This method checks for the email validations like, empty email or email contains invalid characters
     *  {Input} - Email string
     *  {output} - ValidationResult object
     */
    fun validateEmail(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email can't be blank"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Invalid Email"
            )
        }
        return ValidationResult(successful = true)
    }

    /**
     * Method checks for password validation if it contains invalid characters or has invalid length
     * {input} - Password string
     * {output} - ValidationResult object
     */
    fun validatePassword(password: String): ValidationResult {
        if(password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password can't be blank"
            )
        }
        if(password.length < 8 || password.length > 16) {
            return ValidationResult(
                successful = false,
                errorMessage = "Length must be between 8 to 16"
            )
        }

        if(!password.all{ it.isLetterOrDigit() }) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password must contain a number and letter"
            )
        }

        return ValidationResult(successful = true)
    }

    /**
     * Method checks for if confirm password matches the password
     * {input} - Password and Confirm Password strings
     * {output} - ValidationResult object
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        if(password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password doesn't match"
            )
        }

        return ValidationResult(successful = true)
    }
}