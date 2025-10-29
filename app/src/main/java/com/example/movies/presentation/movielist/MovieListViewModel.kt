package com.example.movies.presentation.movielist

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

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<AnimeMovie> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            repository.getAnimeMovies().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = MovieListState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = MovieListState(
                            movies = result.data ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _state.value = MovieListState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }
}
