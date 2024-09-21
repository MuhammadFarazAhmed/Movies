package com.example.movies.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Media
import com.example.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchMoviesViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")
    val media: MutableStateFlow<Map<String, List<Media>>> = MutableStateFlow(mapOf())

    fun onQueryChange(query: String) {
        this.query.value = query

        if (this.query.value.isEmpty()) {
            media.value = mapOf()
        }
    }

    init {
        viewModelScope.launch {
            query.debounce(500L)
                .filter { it.isNotEmpty() }
                .collectLatest {
                    searchMoviesUseCase(it).collectLatest { data ->

                        val currentMovies = media.value.toMutableMap()

                        // requirements
                        val newGroupedMovies = data.filter { media -> media.mediaType != "person" }
                            .sortedBy { media -> media.mediaType }
                            .groupBy { media -> media.mediaType }

                        newGroupedMovies.forEach { (mediaType, newMovieList) ->
                            // Append new movies to the existing list
                            val existingMovies = currentMovies[mediaType].orEmpty()
                            currentMovies[mediaType] = existingMovies + newMovieList
                        }

                        media.value = currentMovies
                    }
                }
        }
    }

}