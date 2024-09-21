package com.example.domain.usecase

import com.example.domain.model.Media
import com.example.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(private val movieRepository: MovieRepository) {

    operator fun invoke(query: String): Flow<List<Media>> =
        movieRepository.searchMovies(query)

}