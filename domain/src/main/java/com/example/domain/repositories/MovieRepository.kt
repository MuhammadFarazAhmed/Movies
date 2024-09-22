package com.example.domain.repositories

import com.example.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun searchMovies(query: String, maxPageThreshold: Int): Flow<List<Media>>
}