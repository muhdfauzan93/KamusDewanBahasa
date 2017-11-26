package com.caliphstudio.kamusdewanbahasa.api

import com.caliphstudio.kamusdewanbahasa.models.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by MuhdFauzan on 11/26/2017.
 */
interface APIInterface {

    @GET("/")
    fun checkWord(@Query("word") word: String): Call<Result>

    @GET("/")
    fun searchHadis(@Query("json") query: String,  @Query("search") searchKey: String): Call<Result>

}