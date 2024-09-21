package com.example.data.mapper

import com.example.data.model.Result
import com.example.data.model.toDomain
import com.example.domain.model.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieMapper {

    fun map(media: Flow<List<Result>>): Flow<List<Media>> =
        media.map { item -> item.map { media -> media.toDomain() } }

}