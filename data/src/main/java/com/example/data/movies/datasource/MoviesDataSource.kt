package com.example.data.movies.datasource

import com.example.data.model.SearchMoviesInput
import com.example.data.model.SearchMoviesResponse

interface MoviesDataSource {

    suspend fun searchMovies(input: SearchMoviesInput): SearchMoviesResponse

}