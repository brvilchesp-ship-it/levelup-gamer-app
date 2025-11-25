package com.example.levelupgamer.repository

import com.example.levelupgamer.remote.UserDto
import com.example.levelupgamer.remote.UserRetrofitInstance

class UserRepository {

    private val api = UserRetrofitInstance.api

    suspend fun registerUser(dto: UserDto): UserDto =
        api.createUser(dto)

    suspend fun getUser(email: String): UserDto =
        api.getUser(email)
}
