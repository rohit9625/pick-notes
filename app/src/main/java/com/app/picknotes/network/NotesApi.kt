package com.app.picknotes.network

import com.app.picknotes.models.Note
import com.app.picknotes.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesApi {
    @GET("/notes")
    suspend fun getNotes() : Response<List<NoteResponse>>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: Note): Response<NoteResponse>

    @PUT("/notes/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId: String, @Body noteRequest: Note): Response<NoteResponse>

    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NoteResponse>
}