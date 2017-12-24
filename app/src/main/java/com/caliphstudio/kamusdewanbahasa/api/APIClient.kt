package com.caliphstudio.kamusdewanbahasa.api

import com.caliphstudio.kamusdewanbahasa.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit



object APIClient {
    private var retrofit: Retrofit? = null
    private val REQ_TIMEOUT:Long = 60


    fun getClient(): Retrofit {

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit!!
    }
}