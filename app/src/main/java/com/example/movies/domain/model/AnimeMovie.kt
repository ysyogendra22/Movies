package com.example.movies.domain.model

import com.example.movies.data.model.AnimeData

data class AnimeMovie(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val score: Double?,
    val year: Int?,
    val synopsis: String?,
    val rating: String?,
    val duration: String?,
    val status: String?,
    val genres: List<String>,
    val studios: List<String>
)

fun AnimeData.toDomain(): AnimeMovie {
    return AnimeMovie(
        id = malId,
        title = title,
        imageUrl = images.jpg.largeImageUrl ?: images.jpg.imageUrl ?: "",
        score = score,
        year = year,
        synopsis = synopsis,
        rating = rating,
        duration = duration,
        status = status,
        genres = genres?.map { it.name } ?: emptyList(),
        studios = studios?.map { it.name } ?: emptyList()
    )
}
