package com.vedic.pagination.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvideRetrofit {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {

        val apiKeyInterceptor = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                var requestBuilder = original.newBuilder().header(
                    "Authorization", "BuildConfig.PEXELS_API_KEY"
                ) // Replace 'YOUR_API_KEY' with your actual API key

                var request = requestBuilder.build()
                var response = chain.proceed(request)
                response
            }

        return Retrofit.Builder()
            .client(apiKeyInterceptor.build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl("BuildConfig.PEXELS_BASE_URL")
            .build()
    }

}