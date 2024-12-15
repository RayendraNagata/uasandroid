package com.example.uasandroid.data

import com.example.uasandroid.model.Note
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("notes")
    fun getAllNotes(): Call<List<Note>>

    @POST("notes")
    fun createNote(
        @Body note: Note
    ): Call<Note>

    @PUT("notes/{id}")
    fun updateNote(
        @Path("id") id: Int,
        @Body note: Note
    ): Call<Note>

    @DELETE("notes/{id}")
    fun deleteNote(
        @Path("id") id: Int
    ): Call<Void>
}
