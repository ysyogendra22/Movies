package com.example.movies.presentation.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.model.AnimeMovie
import com.example.movies.domain.repository.AnimeRepository
import com.example.movies.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: AnimeMovie? = null,
    val error: String = ""
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("movieId")?.let { movieId ->
            loadMovieDetail(movieId)
        }
    }

    private fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            repository.getAnimeMovies().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = MovieDetailState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val movie = result.data?.find { it.id == movieId }
                        _state.value = MovieDetailState(movie = movie)
                    }
                    is Resource.Error -> {
                        _state.value = MovieDetailState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }
}
