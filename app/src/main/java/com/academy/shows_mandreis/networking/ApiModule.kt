package com.academy.shows_mandreis.networking

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy"

    lateinit var retrofit: ShowsApiService

    fun initRetrofit(preferences: SharedPreferences) {
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(
                Interceptor { chain ->
                    var request = chain.request()

                    if (preferences.contains("token")) {
                        request = request.newBuilder()
                            .addHeader("uspjeh", "juhuuu")
                            .build()
                    }

                    chain.proceed(request)
                }
            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}