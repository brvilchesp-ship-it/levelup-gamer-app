package com.example.levelupgamer.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRetrofitInstance {

    val api: ProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")   // 👈 emulador → tu PC
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}
