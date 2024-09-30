package com.app.picknotes.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.app.picknotes.notes.domain.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object NoteNavType: NavType<Note?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Note? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Note::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Note {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Note?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Note?): String {
        return Json.encodeToString(value)
    }
}