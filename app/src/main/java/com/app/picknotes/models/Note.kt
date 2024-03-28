package com.app.picknotes.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val title: String = "",
    val description: String = ""
) : Parcelable
