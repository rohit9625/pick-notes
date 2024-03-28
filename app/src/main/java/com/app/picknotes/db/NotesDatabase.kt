package com.app.picknotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.picknotes.models.NoteResponse

@Database(entities = [NoteResponse::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao
}