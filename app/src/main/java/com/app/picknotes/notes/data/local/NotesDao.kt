package com.app.picknotes.notes.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Upsert
    suspend fun createNote(note: NotesEntity)

    @Update
    suspend fun updateNote(note: NotesEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NotesEntity>>

    @Upsert
    suspend fun addNotes(notes: List<NotesEntity>)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}
