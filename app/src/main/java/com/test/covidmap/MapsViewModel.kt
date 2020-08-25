package com.test.covidmap

import androidx.lifecycle.ViewModel
import com.test.network.ApiFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class MapsViewModel : ViewModel()
{
    fun getDailyData(code: String?) : Any? = runBlocking {
        val res = async {
            if (code != null) {
                ApiFactory.rapidApi.getDailyTotalsAsync(code)
            }
            else
            {
                null
            }
        }
        res.await()
    }
}