package com.app.picknotes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.app.picknotes.models.NoteResponse

@Dao
interface NotesDao {
    @Insert
    suspend fun createNote(note: NoteResponse)

    @Insert
    suspend fun addNotes(notes: List<NoteResponse>)

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<NoteResponse>

    @Update
    suspend fun updateNote(note: NoteResponse)

    @Query("DELETE FROM notes WHERE _id = :noteId")
    suspend fun deleteNote(noteId: String)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}
