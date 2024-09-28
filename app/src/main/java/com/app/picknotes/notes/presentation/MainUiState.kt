package com.app.picknotes.notes.presentation

import com.app.picknotes.notes.domain.Note

data class MainUiState(
    val isLoading: Boolean = false,
    val notes: List<Note> = emptyList(),
    val errorMessage: String? = null
)