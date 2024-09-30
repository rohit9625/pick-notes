package com.app.picknotes.navigation

import com.app.picknotes.notes.domain.Note
import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object HomeScreen: NavRoute

    @Serializable
    data class NewNoteScreen(val note: Note? = null): NavRoute

    @Serializable
    data object SignUpScreen: NavRoute

    @Serializable
    data object SignInScreen: NavRoute
}