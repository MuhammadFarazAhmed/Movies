package com.example.movies.di

import android.util.Log
import com.example.movies.common.NetworkStatusTracker
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module


fun featureModules() = listOf<Module>()


val NetworkModule = module {
    single {
        HttpClient(Android) {

            defaultRequest {
                url {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            install(Logging) { level = LogLevel.ALL }

            install(ContentNegotiation) {
                gson {
                    serializeNulls()
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

