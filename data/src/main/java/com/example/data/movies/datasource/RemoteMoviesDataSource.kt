package com.example.data.movies.datasource

import com.example.data.common.makeApiCall
import com.example.data.model.SearchMoviesInput
import com.example.data.model.SearchMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.path

class RemoteMoviesDataSource(
    private val httpClient: HttpClient,
) : MoviesDataSource {

    override suspend fun searchMovies(input: SearchMoviesInput): SearchMoviesResponse =
        makeApiCall(httpClient) {
            url { path("3/search/multi") }
            parameter("query", input.query)
            parameter("include_adult", true)
            parameter("page", input.page.toString())
        }
}