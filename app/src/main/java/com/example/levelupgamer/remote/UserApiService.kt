package com.example.levelupgamer.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    @POST("/api/users")
    suspend fun createUser(@Body user: UserDto): UserDto

    @GET("/api/users/{email}")
    suspend fun getUser(@Path("email") email: String): UserDto
}
