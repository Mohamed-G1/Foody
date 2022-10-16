package com.example.di

import com.example.data.network.FoodApi
import com.example.utils.Constans.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    // provide okHttp to can provideRetrofitInstance use it
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }


    // also provide gsonConverterFactory to can provideRetrofitInstance use it
    @Singleton
    @Provides
    fun provideGsonFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    // provide retrofit instance to provideApiService fun
    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    }

    // provide api service that retrofit can use it
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FoodApi {
        return retrofit.create(FoodApi::class.java)
    }
}