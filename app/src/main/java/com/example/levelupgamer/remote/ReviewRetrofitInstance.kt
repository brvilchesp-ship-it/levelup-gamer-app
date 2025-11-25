package com.example.levelupgamer.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReviewRetrofitInstance {

    val api: ReviewApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewApiService::class.java)
    }
}
