package com.app.picknotes.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotesEntity::class], version = 2)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao
}