package com.example.data.common

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

suspend inline fun <reified T> makeApiCall(
    httpClient: HttpClient,
    block: HttpRequestBuilder.() -> Unit
): T {
    val response = httpClient.get {
        block()
    }

    return when (response.status.value) {
        in 200..299 -> {
            response.body<T>()
        }

        else -> {
            val errorBody = response.bodyAsText()
            println("Error response: $errorBody")
            throw Exception("API error: ${response.status.value} - $errorBody")
        }
    }
}

fun unsupported(message: String? = null): Nothing = throw UnsupportedOperationException(message)