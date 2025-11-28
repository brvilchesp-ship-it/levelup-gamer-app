package com.example.levelupgamer.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CartRetrofitInstance {

    // 👉 Cambia esto por la URL de tu backend (local, AWS, etc.)
    // Ej: "http://10.0.2.2:9090/" para emulador Android con backend local
    // o "http://TU_IP_EC2:9090/" si está en AWS
    private const val BASE_URL = "http://10.0.2.2:9090/"

    private val client = OkHttpClient.Builder().build()

    val api: CartApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartApiService::class.java)
    }
}
