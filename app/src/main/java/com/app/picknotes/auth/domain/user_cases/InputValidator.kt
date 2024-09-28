package com.app.picknotes.auth.domain.user_cases

import com.app.picknotes.auth.domain.model.InputValidationError
import com.app.picknotes.auth.domain.model.Result
import javax.inject.Inject

class InputValidator @Inject constructor() {

    fun validateBlankCheck(
        username: String, email: String, password: String, confirmPassword: String
    ): Result<Unit, InputValidationError.GeneralError> {
        if(username.isBlank() ||  email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return Result.Error(InputValidationError.GeneralError.MANDATORY)
        }

        return Result.Success(Unit)
    }

    fun validateBlankCheck(email: String, password: String): Result<Unit, InputValidationError.GeneralError> {
        if(email.isBlank() || password.isBlank()) {
            return Result.Error(InputValidationError.GeneralError.MANDATORY)
        }

        return Result.Success(Unit)
    }

    /**
     * This method checks for the name if it's empty, is invalid(contains numbers or any other chars) and for length
     *  [username] - Name as string
     *  [Result] - Result object with [Unit] when success and [InputValidationError.UsernameError] when fails
     */
    fun validateUsername(username: String): Result<Unit, InputValidationError.UsernameError> {
        return if (username.isBlank()) {
            Result.Error(InputValidationError.UsernameError.USERNAME_MANDATORY)
        }
        else if (username.length > 16) {
            Result.Error(InputValidationError.UsernameError.USERNAME_TOO_LONG)
        }
        else if(username.length < 5) {
            Result.Error(InputValidationError.UsernameError.USERNAME_TOO_SHORT)
        }
        else if (!username.contains(Regex("^[a-zA-Z]+(\\s[a-zA-Z]+)*$"))) {
            Result.Error(InputValidationError.UsernameError.WHITESPACE_NOT_ALLOWED)
        }
        else {
            Result.Success(Unit)
        }
    }

    /**
     * This method checks for the email validations like, empty email or email contains invalid characters
     *  [email] - Email as string
     *  [Result] - Result object with [Unit] when success and [InputValidationError.EmailError] when fails
     */
    fun validateEmail(email: String): Result<Unit, InputValidationError.EmailError> {
        return if(email.isBlank()) {
            Result.Error(InputValidationError.EmailError.EMAIL_MANDATORY)
        }
        else if(!email.matches(Regex("^[a-zA-Z][a-zA-Z0-9._%+-]{0,64}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,32}\\.[a-zA-Z]{2,6}$"))) {
            Result.Error(InputValidationError.EmailError.EMAIL_INVALID)
        } else {
            Result.Success(Unit)
        }
    }

    /**
     * Method checks for password validation if it contains invalid characters or has invalid length
     * [password] - Input given to the method
     * [Result] - Result with [Unit] when success and [InputValidationError.PasswordError] when fails
     */
    fun validatePassword(password: String): Result<Unit, InputValidationError.PasswordError> {
        return if(password.isBlank()) {
            Result.Error(InputValidationError.PasswordError.PASSWORD_MANDATORY)
        }
        else if(password.contains(Regex("\\s"))) {
            Result.Error(InputValidationError.PasswordError.WHITESPACE_NOT_ALLOWED)
        }
        else if(password.length < 8 || password.length > 16) {
            Result.Error(InputValidationError.PasswordError.PASSWORD_LENGTH)
        }
        else if(!password.contains(Regex("[A-Za-z]"))) {
            Result.Error(InputValidationError.PasswordError.MISSING_ALPHABET)
        }
        else if(!password.contains(Regex("\\d"))) {
            Result.Error(InputValidationError.PasswordError.MISSING_NUMBER)
        }
        else if(!password.contains(Regex("[$#@]"))) {
            Result.Error(InputValidationError.PasswordError.MISSING_SYMBOL)
        }
        else Result.Success(Unit)
    }

    /**
     * Method checks for if confirm password matches the password
     * [password] - Password to be check with the confirm password
     * [confirmPassword] - Confirm password to check with the original password
     * [Result] - Result with [Unit] when success and [InputValidationError.PasswordError] when fails
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): Result<Unit, InputValidationError.PasswordError> {
        return if(password != confirmPassword) {
            Result.Error(InputValidationError.PasswordError.PASSWORD_NOT_MATCHED)
        } else {
            Result.Success(Unit)
        }
    }
}