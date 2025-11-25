package com.example.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelupgamer.remote.ExternalRetrofitInstance
import com.example.levelupgamer.repository.ExternalPostRepository

class ExternalPostViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExternalPostViewModel::class.java)) {

            // Repo que usa la API externa (JSONPlaceholder)
            val repository = ExternalPostRepository(
                api = ExternalRetrofitInstance.api
            )

            return ExternalPostViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

