package com.caliphstudio.kamusdewanbahasa.api

import com.caliphstudio.kamusdewanbahasa.Config
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Created by MuhdFauzan on 11/26/2017.
 */
object APIClient {


    private var retrofit: Retrofit? = null

    var gson = GsonBuilder().setLenient().create()

    fun getClient(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


        retrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()



        return retrofit!!
    }
}