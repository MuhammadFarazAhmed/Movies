package com.example.domain.usecase

import com.example.domain.model.Media
import com.example.domain.repositories.MovieRepository
import com.google.android.material.transition.MaterialContainerTransform.ProgressThresholds
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(private val movieRepository: MovieRepository) {

    operator fun invoke(query: String, maxPageThresholds: Int): Flow<List<Media>> =
        movieRepository.searchMovies(query, maxPageThresholds)

}