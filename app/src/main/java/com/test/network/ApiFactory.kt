package com.test.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory
{
    private val authInterceptor = Interceptor{ chain ->

        val request = chain.request().newBuilder()
            .addHeader("x-rapidapi-host", "covid-19-global-tracker-with-regional-data.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "46bf2df827msh1f6414ea8bf7731p16a960jsnfe2e28c0b243")
            .addHeader("x-authorization", "6179002e-6646-4852-be37-572758a58cbb")
            .build()
        chain.proceed(request)
    }

    private val httpInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }


    private val httpClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor( httpInterceptor)
        .build()

    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://covid-19-global-tracker-with-regional-data.p.rapidapi.com/api/covid/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val rapidApi = retrofit().create(RapidApi::class.java)
}