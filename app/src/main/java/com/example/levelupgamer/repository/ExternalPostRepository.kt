package com.example.levelupgamer.repository

import com.example.levelupgamer.model.ExternalPost
import com.example.levelupgamer.remote.ExternalApiService

class ExternalPostRepository(
    private val api: ExternalApiService
) {
    suspend fun getPosts(): List<ExternalPost> {
        return api.getPosts()
    }
}
