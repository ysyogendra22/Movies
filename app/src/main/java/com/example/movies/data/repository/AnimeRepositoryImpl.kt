package com.example.movies.data.repository

import com.example.movies.data.remote.JikanApiService
import com.example.movies.domain.model.AnimeMovie
import com.example.movies.domain.model.toDomain
import com.example.movies.domain.repository.AnimeRepository
import com.example.movies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApiService
) : AnimeRepository {

    override suspend fun getAnimeMovies(): Flow<Resource<List<AnimeMovie>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAnimeMovies()
            val movies = response.data.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server. Check your internet connection."
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred"
            ))
        }
    }
}
