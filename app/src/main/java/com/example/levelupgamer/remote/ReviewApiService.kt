package com.example.levelupgamer.remote

import com.example.levelupgamer.data.Review
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApiService {

    @GET("/api/reviews/product/{productId}")
    suspend fun getByProduct(@Path("productId") productId: String): List<Review>

    @POST("/api/reviews")
    suspend fun createReview(@Body review: Review): Review
}
