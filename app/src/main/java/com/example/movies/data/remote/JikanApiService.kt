package com.example.movies.data.remote

import com.example.movies.data.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {
    @GET("anime")
    suspend fun getAnimeMovies(
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): AnimeResponse
}
