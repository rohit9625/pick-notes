package com.app.picknotes.repository

import android.util.Log
import com.app.picknotes.db.NotesDao
import com.app.picknotes.models.Note
import com.app.picknotes.models.NoteResponse
import com.app.picknotes.network.NotesApi
import com.app.picknotes.utils.Constants.TAG
import com.app.picknotes.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesApi: NotesApi,
    private val notesDao: NotesDao
) {

    suspend fun getNotes(): NetworkResult<List<NoteResponse>> {
        return try {
            val notes = notesDao.getNotes()

            if(notes.isEmpty()) {
                val response = notesApi.getNotes()
                if(response.isSuccessful && response.body() != null) {
                    notesDao.addNotes(response.body()!!)
                    NetworkResult.Success(data = response.body()!!)
                }else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Log.e(TAG, "Error while fetching notes : $errorMessage")
                    NetworkResult.Error(message = errorMessage)
                }
            }else {
                NetworkResult.Success(data = notes)
            }
        }catch (e: Exception) {
            Log.e(TAG, "Unexpected error caught when fetching notes : $e")
            NetworkResult.Error(message = e.message)
        }
    }
    suspend fun createNote(note: Note): NetworkResult<NoteResponse> {
        return try {
            val response = notesApi.createNote(note)

            if(response.isSuccessful && response.body() != null) {
                notesDao.createNote(response.body()!!)
                Log.d(TAG, "Note created : ${response.body()}")
                NetworkResult.Success(data = response.body())
            }else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = JSONObject(errorBody).getString("message")
                Log.e(TAG, "Error while creating a new note : $errorMessage")
                NetworkResult.Error(message = errorMessage)
            }
        }catch (e: Exception) {
            Log.e(TAG, "Unexpected error caught when creating note : $e")
            NetworkResult.Error(e.message)
        }
    }

    suspend fun updateNote(noteId: String, note: Note): NetworkResult<NoteResponse> {
        return try {
            val response = notesApi.updateNote(noteId, note)

            if(response.isSuccessful && response.body() != null) {
                notesDao.updateNote(response.body()!!)
                Log.d(TAG, "Note Updated Successfully : ${response.body()}")
                NetworkResult.Success(data = response.body())
            }else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = JSONObject(errorBody).getString("message")
                Log.e(TAG, "Error while updating note : $errorMessage")
                NetworkResult.Error(message = errorMessage)
            }
        }catch (e: Exception) {
            Log.e(TAG, "Unexpected error caught when creating note : $e")
            NetworkResult.Error(e.message)
        }
    }

    suspend fun deleteNote(noteId: String): NetworkResult<String> {
        return try {
            val response = notesApi.deleteNote(noteId)

            return if(response.isSuccessful && response.body() != null) {
                notesDao.deleteNote(noteId)
                Log.d(TAG, "Note deleted : ${response.body()}")
                NetworkResult.Success(data = "Note deleted successfully")
            }else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = JSONObject(errorBody).getString("message")
                Log.e(TAG, "Error while deleting notes : $errorMessage")
                NetworkResult.Error(message = errorMessage)
            }
        }catch (e: Exception) {
            Log.e(TAG, "Unexpected error caught when deleting note : $e")
            NetworkResult.Error(e.message)
        }
    }

    suspend fun deleteAllNotes(): NetworkResult<String> {
        return try {
            notesDao.deleteAllNotes()
            Log.d(TAG, "All notes deleted successfully")
            NetworkResult.Success("All notes deleted successfully")
        }catch (e: Exception) {
            Log.e(TAG, "Error while deleting all notes")
            NetworkResult.Error(e.message)
        }        }
}