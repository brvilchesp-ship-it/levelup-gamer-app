package com.example.levelupgamer.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRetrofitInstance {

    val api: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // mismo que productos
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }
}
