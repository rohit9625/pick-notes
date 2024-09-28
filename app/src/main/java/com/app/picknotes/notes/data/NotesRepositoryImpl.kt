package com.app.picknotes.notes.data

import com.app.picknotes.notes.data.local.NotesDao
import com.app.picknotes.notes.data.local.NotesEntity
import com.app.picknotes.notes.data.mapper.NotesMapper
import com.app.picknotes.notes.data.remote.NotesApi
import com.app.picknotes.notes.domain.Note
import com.app.picknotes.notes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesApi: NotesApi,
    private val notesDao: NotesDao,
    private val mapper: NotesMapper
): NotesRepository {
    override fun fetchNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map { entities->
            entities.map { mapper.mapToNote(it) }
        }
    }

    override suspend fun createNote(title: String, description: String) {
        val currentTimeMillis = System.currentTimeMillis()
        val entity = NotesEntity(
            title = title,
            description = description,
            updatedAt = currentTimeMillis,
            createdAt = currentTimeMillis
        )
        notesDao.createNote(entity)
    }

    override suspend fun updateNote(note: Note) {
        val entity = mapper.mapToEntity(note)
        notesDao.updateNote(entity.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }
}