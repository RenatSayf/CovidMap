package com.test.covidmap

import androidx.lifecycle.ViewModel
import com.test.network.ApiFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class MapsViewModel : ViewModel()
{
    fun getDailyData(code: String) : Any = runBlocking {
        val res = async {
            ApiFactory.rapidApi.getDailyTotalsAsync(code)
        }
        res.await()
    }
}