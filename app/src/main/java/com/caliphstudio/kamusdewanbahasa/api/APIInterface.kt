package com.caliphstudio.kamusdewanbahasa.api

import com.caliphstudio.kamusdewanbahasa.models.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("/")
    fun checkWord(@Query("word") word: String): Call<Result>

}