package com.app.picknotes.notes.domain.use_cases

import com.app.picknotes.notes.domain.Note
import com.app.picknotes.notes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    fun getAllNotes(): Flow<List<Note>> {
        return notesRepository.fetchNotes()
    }

    suspend fun createNote(title: String, description: String) {
        notesRepository.createNote(title, description)
    }

    suspend fun deleteNote(noteId: Int) {
        notesRepository.deleteNote(noteId)
    }

    suspend fun updateNote(note: Note) {
        notesRepository.updateNote(note)
    }
}