package com.app.picknotes.notes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesEntity(
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
)
