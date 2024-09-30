package com.app.picknotes.notes.presentation

data class NewNoteState(
    val id: Int? = null, // New notes wouldn't have any id
    val title: String = "",
    val description: String = ""
)
