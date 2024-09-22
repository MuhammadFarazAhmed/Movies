package com.example.data.model

import com.example.domain.model.Media
import com.google.gson.annotations.SerializedName

data class SearchMoviesResponse(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("results")
    val results: List<Result> = emptyList(),
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int?
)

data class Result(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("known_for")
    val knownFor: List<KnownFor> = emptyList(),
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    @SerializedName("overview")
    val overview: String? = "",
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int?>?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("origin_country")
    val originCountry: List<String?>?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("video")
    val video: Boolean = false
)

fun Result.toDomain() = Media(
    id,
    if (mediaType == "tv") name else title,
    overview,
    video,
    backdropPath,
    mediaType,
    adult
)

data class KnownFor(
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("title")
    val title: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("media_type")
    val mediaType: String = "",
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int?>?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("video")
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?
)

