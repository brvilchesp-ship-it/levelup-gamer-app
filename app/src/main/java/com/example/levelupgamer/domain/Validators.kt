
package com.example.levelupgamer.domain

object Validators {
    fun nameOk(s: String) = s.trim().length >= 2
    fun emailOk(s: String) = s.contains("@") && s.contains(".")
    fun ageOk(n: Int?) = (n ?: 0) >= 18
}
