package com.app.picknotes.notes.domain

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun fetchNotes(): Flow<List<Note>>

    suspend fun createNote(title: String, description: String)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(noteId: Int)
}