package com.example.movies.domain.repository

import com.example.movies.domain.model.AnimeMovie
import com.example.movies.util.Resource
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeMovies(): Flow<Resource<List<AnimeMovie>>>
}
