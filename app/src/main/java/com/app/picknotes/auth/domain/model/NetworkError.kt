package com.app.picknotes.auth.domain.model

sealed interface NetworkError: Error {
    enum class UserError: NetworkError {
        USER_NOT_EXISTS,
        USER_ALREADY_EXISTS,
        INVALID_CREDENTIALS,
        UNKNOWN_ERROR,
        SERVER_ERROR
    }
}