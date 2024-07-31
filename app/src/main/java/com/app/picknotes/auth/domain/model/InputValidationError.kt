package com.app.picknotes.auth.domain.model

sealed interface InputValidationError: Error {
    enum class GeneralError: InputValidationError {
        MANDATORY,
        INVALID
    }

    enum class UsernameError: InputValidationError {
        USERNAME_MANDATORY,
        WHITESPACE_NOT_ALLOWED,
        USERNAME_TOO_SHORT,
        USERNAME_TOO_LONG
    }

    enum class PasswordError: InputValidationError {
        PASSWORD_MANDATORY,
        WHITESPACE_NOT_ALLOWED,
        PASSWORD_LENGTH,
        MISSING_ALPHABET,
        MISSING_NUMBER,
        MISSING_SYMBOL,
        PASSWORD_NOT_MATCHED
    }

    enum class EmailError: InputValidationError {
        EMAIL_MANDATORY,
        EMAIL_INVALID
    }
}