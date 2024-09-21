package com.example.data.movies.datasource

import com.example.data.common.unsupported
import com.example.data.model.SearchMoviesInput
import com.example.data.model.SearchMoviesResponse

class LocalMoviesDataSource : MoviesDataSource {

    override suspend fun searchMovies(input: SearchMoviesInput): SearchMoviesResponse {
        unsupported()
    }

}