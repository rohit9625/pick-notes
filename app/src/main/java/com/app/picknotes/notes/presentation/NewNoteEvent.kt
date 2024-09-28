package com.app.picknotes.notes.presentation

import com.app.picknotes.notes.domain.Note

interface NewNoteEvent {
    data class OnTitleChange(val text: String): NewNoteEvent
    data class OnDescriptionChange(val text: String): NewNoteEvent
    data object OnSaveNote: NewNoteEvent
}