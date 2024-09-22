package com.example.movies.vm

import androidx.lifecycle.ViewModel
import com.example.domain.model.Media
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaDetailViewModel() : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsState> = MutableStateFlow(MovieDetailsState(Media()))
    val uiState = _uiState.asStateFlow()

}

data class MovieDetailsState(
    var media: Media
)