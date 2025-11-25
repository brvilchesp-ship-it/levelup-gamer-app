package com.example.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.model.ExternalPost
import com.example.levelupgamer.repository.ExternalPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExternalPostViewModel(
    private val repo: ExternalPostRepository
) : ViewModel() {

    private val _postList = MutableStateFlow<List<ExternalPost>>(emptyList())
    val postList: StateFlow<List<ExternalPost>> = _postList

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                _postList.value = repo.getPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
