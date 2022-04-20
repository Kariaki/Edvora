package com.test.module

import com.test.edvora.Constants
import com.test.edvora.api.EdvoraApi
import com.test.edvora.dao.EdvoraDao
import com.test.edvora.model.User
import com.test.edvora.repositories.EdvoraRepository
import com.test.edvora.repositories.FakeEdvoraRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object EdvoraModule {

    @Provides
    fun retrofitInstance(client: OkHttpClient): EdvoraApi = Retrofit.Builder()
        .baseUrl(Constants.baseUrl2)
        .addConverterFactory(GsonConverterFactory
            .create())
        .client(client)
        .build()
        .create(EdvoraApi::class.java)

    @Provides
    fun client(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    fun interceptor(): Interceptor = HttpLoggingInterceptor()

    @Provides
    @Named("repository")
    fun provideRepository( api: EdvoraApi):EdvoraDao = EdvoraRepository(api)

    @Provides
    @Named("fakeRepository")
    fun provideFakeRepository():EdvoraDao = FakeEdvoraRepository()

}