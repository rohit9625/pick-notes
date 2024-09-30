package com.app.picknotes.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.picknotes.notes.domain.Note
import com.app.picknotes.notes.domain.use_cases.NotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val notesUseCase: NotesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(NewNoteState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: NewNoteEvent) {
        when(e) {
            is NewNoteEvent.OnTitleChange -> _uiState.update { it.copy(title = e.text) }
            is NewNoteEvent.OnDescriptionChange -> _uiState.update { it.copy(description = e.text) }
            is NewNoteEvent.OnSaveNote -> saveNote()
        }
    }

    fun initEdit(note: Note) {
        _uiState.update { it.copy(id = note.id, title = note.title, description = note.description) }
    }

    private fun saveNote() {
        viewModelScope.launch {
            notesUseCase.createNote(
                title = _uiState.value.title, description = _uiState.value.description)
        }
    }
}