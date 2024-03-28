package com.app.picknotes.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.picknotes.models.Note
import com.app.picknotes.models.NoteResponse
import com.app.picknotes.repository.NotesRepository
import com.app.picknotes.utils.Constants.TAG
import com.app.picknotes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
): ViewModel() {
    private val _notesState = MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading())
    val notesState = _notesState

    var newNoteTitle by mutableStateOf("")
        private set
    var newNoteDescription by mutableStateOf("")
        private set
    var isNoteSaved by mutableStateOf(false)

    init {
        fetchNotes()
    }

    fun updateNewNoteTitle(text: String) {
        newNoteTitle = text
    }

    fun updateNewNoteDescription(text: String) {
        newNoteDescription = text
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            val result = notesRepository.getNotes()
            _notesState.emit(result)
        }
    }

    fun createNote() {
        val note = Note(id = "", title = newNoteTitle, description = newNoteDescription)
        viewModelScope.launch {
            try {
                val result = notesRepository.createNote(note)
                if(result is NetworkResult.Success) {
                    isNoteSaved = true
                    fetchNotes()
                } else if (result is NetworkResult.Error) {
                    // Log error message or handle the error appropriately
                    Log.e(TAG, "Failed to create note: ${result.message}")
                }
            } catch (e: Exception) {
                // Log any unexpected exception
                Log.e(TAG, "Unexpected error when creating note", e)
            }
        }
    }

    fun editNote(title: String, description: String) {
        newNoteTitle = title
        newNoteDescription = description
    }

    fun updateNote(noteId: String) {
        viewModelScope.launch {
            notesRepository.updateNote(noteId, Note(newNoteTitle, newNoteDescription))
            fetchNotes()
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)

            fetchNotes()
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            notesRepository.deleteAllNotes()
        }
    }
}