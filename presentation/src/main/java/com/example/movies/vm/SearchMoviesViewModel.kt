package com.example.movies.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Media
import com.example.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchMoviesViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private var job: Job = Job()

    private val query: MutableStateFlow<String> = MutableStateFlow("")

    private val _uiState: MutableStateFlow<SearchMediaState> = MutableStateFlow(SearchMediaState())
    val uiState = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        //  if api is calling cancel it to call with new query
        if (job.isActive) {
            job.cancel()
            searchMedia()
        }

        this.query.value = query

        _uiState.update {
            it.copy(media = mapOf())
        }
        if (query.isEmpty()) {
            _uiState.update { it.copy(defaultState = true) }
        }
    }

    private fun searchMedia() {
        job = viewModelScope.launch {
            query.debounce(500L)
                .filter { it.isNotEmpty() } // Only proceed if query is not empty
                .onEach { query ->
                    if (query.isNotEmpty()) {
                        _uiState.update {
                            it.copy(loading = true, defaultState = false)
                        }
                    }
                }
                .collectLatest { query ->
                    searchMoviesUseCase(
                        query = query,
                        maxPageThresholds = 50 // This is where we changes data fetches to our needs , currently getting 50 pages
                    ).collectLatest { data ->
                        updateMediaList(data)
                    }
                }
        }
    }

    private fun updateMediaList(
        data: List<Media>
    ) {
        val currentMedia = _uiState.value.media.toMutableMap()

        val newGroupedMedia = data.filter { media -> media.mediaType != "person" }
            .sortedBy { media -> media.mediaType }
            .groupBy { media -> media.mediaType }

        newGroupedMedia.forEach { (mediaType, mediaList) ->
            if (mediaType == "movie") {
                val adultMovies = mediaList.filter { it.adult ?: false }
                val nonAdultMovies = mediaList.filterNot { it.adult ?: false }

                if (adultMovies.isNotEmpty()) {
                    currentMedia["xxx"] = currentMedia["xxx"].orEmpty() + adultMovies
                }

                currentMedia["movie"] = currentMedia["movie"].orEmpty() + nonAdultMovies
            } else {
                // Append other media types
                val existingMedia = currentMedia[mediaType].orEmpty()
                currentMedia[mediaType] = existingMedia + mediaList
            }
        }

        _uiState.update {
            it.copy(
                loading = false,
                media = currentMedia,
                emptyView = currentMedia.isEmpty()
            )
        }
    }
}

data class SearchMediaState(
    val defaultState: Boolean = true,
    val emptyView: Boolean = false,
    val loading: Boolean = false,
    val media: Map<String, List<Media>> = mapOf(),
)