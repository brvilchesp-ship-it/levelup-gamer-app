package com.example.levelupgamer.repository

import com.example.levelupgamer.data.Review
import com.example.levelupgamer.remote.ReviewRetrofitInstance

class ReviewRepository {

    private val api = ReviewRetrofitInstance.api

    suspend fun getByProduct(productId: String): List<Review> =
        api.getByProduct(productId)

    suspend fun createReview(review: Review): Review =
        api.createReview(review)
}
