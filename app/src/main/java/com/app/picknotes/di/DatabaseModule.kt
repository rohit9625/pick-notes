package com.app.picknotes.di

import android.content.Context
import androidx.room.Room
import com.app.picknotes.notes.data.local.NotesDao
import com.app.picknotes.notes.data.local.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context) : NotesDatabase {
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "notes_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNotesDao(database: NotesDatabase): NotesDao {
        return database.notesDao()
    }
}