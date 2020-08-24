package com.test.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query

interface RapidApi
{
    @GET("dailyTotals/{code}")
    fun getDailyTotalsAsync(@Path("code") code: String) : Deferred<Response<Any>>
}