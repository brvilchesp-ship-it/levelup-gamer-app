package com.example.levelupgamer.remote

data class UserDto(
    val email: String,
    val name: String,
    val duoc: Boolean,
    val points: Int = 0
)
