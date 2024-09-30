package com.app.picknotes.notes.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Note(
    val id: Int,
    val title: String,
    val description: String
) : Parcelable
