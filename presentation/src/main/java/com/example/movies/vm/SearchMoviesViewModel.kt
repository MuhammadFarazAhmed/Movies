package com.example.movies.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Media
import com.example.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchMoviesViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private var job: Job = Job()
    private val query = MutableStateFlow("")
    val media: MutableStateFlow<Map<String, List<Media>>> = MutableStateFlow(mapOf())


    fun onQueryChange(query: String) {
        if (job.isActive) {
            job.cancel()
            observeQuery()
        }
        this.query.value = query

        if (this.query.value.isEmpty()) {
            media.value = mapOf()
        }
    }

    private fun observeQuery() {
        job = viewModelScope.launch {
            query.debounce(500L)
                .filter { it.isNotEmpty() }
                .collectLatest { query ->
                    searchMoviesUseCase(
                        query = query,
                        maxPageThresholds = 50 // Means it will go up to 5 pages
                    ).collectLatest { data ->
                        val currentMedia = media.value.toMutableMap()

                        // requirements
                        val newGroupedMedia = data.filter { media -> media.mediaType != "person" }
                            .sortedBy { media -> media.mediaType }
                            .groupBy { media -> media.mediaType }


                        newGroupedMedia.forEach { (mediaType, mediaList) ->
                            if (mediaType == "movie") {
                                // Separate adult and non-adult movies
                                val adultMovies = mediaList.filter { it.adult ?: false }
                                val nonAdultMovies = mediaList.filterNot { it.adult ?: false }

                                // Update the "xxx" category with adult movies, if any
                                if (adultMovies.isNotEmpty()) {
                                    val existingXXX = currentMedia["xxx"].orEmpty()
                                    currentMedia["xxx"] = existingXXX + adultMovies
                                }

                                // Update the movie category with non-adult movies
                                val existingMovies = currentMedia["movie"].orEmpty()
                                currentMedia["movie"] = existingMovies + nonAdultMovies
                            } else {
                                // For other media types, just append the media
                                val existingMedia = currentMedia[mediaType].orEmpty()
                                currentMedia[mediaType] = existingMedia + mediaList
                            }
                        }

                        media.value = currentMedia
                    }
                }
        }
    }

}

data class SearchMediaState(
    val query: String = "",
    val media: Map<String, List<Media>> = mapOf(),
)