package com.app.picknotes.notes.data.mapper

import com.app.picknotes.notes.data.local.NotesEntity
import com.app.picknotes.notes.domain.Note
import javax.inject.Inject

class NotesMapper @Inject constructor() {
    fun mapToNote(notesEntity: NotesEntity) = Note(
        id = notesEntity.id!!, // id will always exist
        title = notesEntity.title,
        description = notesEntity.description
    )

    fun mapToEntity(note: Note): NotesEntity {
        return NotesEntity(
            id = note.id,
            title = note.title,
            description = note.description,
            createdAt = 0L,
            updatedAt = 0L
        )
    }
}