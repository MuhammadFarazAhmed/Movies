package com.example.movies.di

import android.util.Log
import com.example.data.mapper.MovieMapper
import com.example.data.movies.datasource.LocalMoviesDataSource
import com.example.data.movies.datasource.MoviesDataSource
import com.example.data.movies.datasource.RemoteMoviesDataSource
import com.example.data.movies.repositories.MoviesRepositoryImp
import com.example.domain.repositories.MovieRepository
import com.example.domain.usecase.SearchMoviesUseCase
import com.example.movies.BuildConfig
import com.example.movies.vm.MediaDetailViewModel
import com.example.movies.vm.SearchMoviesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun featureModules() = listOf(mediaModule , mediaDetailModule)

val AppModule = module {
    single { Dispatchers.IO }
    single { CoroutineScope(get()) }
}

val NetworkModule = module {
    single {
        HttpClient(Android) {

            defaultRequest {
                url {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                    protocol = URLProtocol.HTTPS
                    host = BuildConfig.API_URL
                }
            }

            install(Logging) { level = LogLevel.ALL }

            install(ContentNegotiation) {
                gson {
                    serializeNulls()
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            BuildConfig.TMDB_TOKEN,
                            BuildConfig.TMDB_TOKEN
                        )
                    }
                }
            }

            HttpResponseValidator {
                validateResponse {
                    when (it.status.value) {
                        in 300..399 -> {}
                        in 400..499 -> {
                            Log.d("TAG", "${it.status.value}")
                        }

                        in 500..599 -> {}
                    }
                }
                handleResponseExceptionWithRequest { cause: Throwable, request: HttpRequest ->
                    Log.d("TAG", "$cause: $request")
                }
            }

            install(ResponseObserver) {
                onResponse { response ->
                    println("HTTP status: ${response.status.value}")
                }
            }

        }
    }
}

val mediaModule = module {
    single { MovieMapper() }

    single<MoviesDataSource>(named("LocalMoviesDataSource")) { LocalMoviesDataSource() }

    single<MoviesDataSource>(named("RemoteMoviesDataSource")) { RemoteMoviesDataSource(get()) }

    single<MovieRepository> {
        MoviesRepositoryImp(
            movieMapper = get(),
            ioDispatcher = get(),
            remoteMoviesDataSource = get(named("RemoteMoviesDataSource")),
            localMoviesDataSource = get(named("LocalMoviesDataSource")),
        )
    }

    single { SearchMoviesUseCase(get()) }

    viewModel { SearchMoviesViewModel(get()) }
}

val mediaDetailModule = module {

    viewModel { MediaDetailViewModel() }
}