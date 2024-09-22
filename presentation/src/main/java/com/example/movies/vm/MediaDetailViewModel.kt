package com.example.movies.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaDetailViewModel() : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsState> = MutableStateFlow(MovieDetailsState())
    val uiState = _uiState.asStateFlow()

}

data class MovieDetailsState(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
)