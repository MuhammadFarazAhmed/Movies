package com.example.data.movies.repositories

import com.example.data.mapper.MovieMapper
import com.example.data.model.Result
import com.example.data.model.SearchMoviesInput
import com.example.data.movies.datasource.MoviesDataSource
import com.example.domain.model.Media
import com.example.domain.repositories.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.min

class MoviesRepositoryImp(
    private val movieMapper: MovieMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val remoteMoviesDataSource: MoviesDataSource,
    private val localMoviesDataSource: MoviesDataSource
) : MovieRepository {

    override fun searchMovies(query: String, maxPageThreshold: Int): Flow<List<Media>> {
        // Convert it to domain model from server model
        return movieMapper.map(fetchPaginatedData(query, maxPageThreshold))
    }

    private fun fetchPaginatedData(query: String, maxPageThreshold: Int): Flow<List<Result>> =
        flow {
            // Hit first time api to get total pages from response
            val firstPageResponse =
                remoteMoviesDataSource.searchMovies(SearchMoviesInput(query = query))
            val totalPages = firstPageResponse.totalPages

            emit(firstPageResponse.results)

            // Once we get total pages . now we call api in  batch of 10 to get results and emit data accordingly
            var page = 2

            // Set MaxPageThreshold to prevent exhaustion of api calls.
            val endPage = min(totalPages, maxPageThreshold)
            while (page <= endPage) {
                val currentBatch =
                    fetchBatchPages(
                        remoteMoviesDataSource,
                        query,
                        page,
                        minOf(page + 9, endPage)
                    )

                // Emit the data of the current batch
                emit(currentBatch)

                // Move to the next batch of 10
                page += 10
            }
        }

    private suspend fun fetchBatchPages(
        remoteMoviesDataSource: MoviesDataSource,
        query: String,
        startPage: Int,
        endPage: Int
    ): List<Result> = (startPage..endPage).map { page ->
        CoroutineScope(ioDispatcher).async {
            runCatching {
                remoteMoviesDataSource.searchMovies(
                    SearchMoviesInput(
                        query,
                        page
                    )
                ).results
            }
                .getOrElse { emptyList() }
        }
    }.awaitAll().flatten()

}
