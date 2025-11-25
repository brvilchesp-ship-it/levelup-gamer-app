package com.example.levelupgamer.remote

import com.example.levelupgamer.model.ExternalPost
import retrofit2.http.GET

interface ExternalApiService {
    @GET("posts")
    suspend fun getPosts(): List<ExternalPost>
}
