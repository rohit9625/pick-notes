package com.app.picknotes.di

import android.content.Context
import com.app.picknotes.network.AuthInterceptor
import com.app.picknotes.network.NotesApi
import com.app.picknotes.auth.data.remote.UserApi
import com.app.picknotes.utils.Constants.BASE_URL
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
class NetworkModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

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

}