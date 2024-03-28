package com.app.picknotes.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteResponse(
    @PrimaryKey(autoGenerate = true) val noteId: Int,
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userID: String
)