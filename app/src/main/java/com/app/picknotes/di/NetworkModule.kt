package com.app.picknotes.di

import android.content.Context
import com.app.picknotes.notes.data.remote.AuthInterceptor
import com.app.picknotes.notes.data.remote.NotesApi
import com.app.picknotes.auth.data.remote.UserApi
import com.app.picknotes.notes.data.NotesRepositoryImpl
import com.app.picknotes.notes.data.local.NotesDao
import com.app.picknotes.notes.data.mapper.NotesMapper
import com.app.picknotes.notes.domain.NotesRepository
import com.app.picknotes.utils.Constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder() : Builder {
        return Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofitBuilder: Builder) : UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNotesApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): NotesApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotesRepository(
        notesApi: NotesApi,
        notesDao: NotesDao,
        noteMapper: NotesMapper
    ): NotesRepository {
        return NotesRepositoryImpl(notesApi, notesDao, noteMapper)
    }
}