package com.example.movies.presentation.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.movies.domain.model.AnimeMovie
import com.example.movies.domain.repository.AnimeRepository
import com.example.movies.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AnimeRepository
    private lateinit var viewModel: MovieListViewModel

    private val testMovie = AnimeMovie(
        id = 1,
        title = "Test Movie",
        imageUrl = "https://test.com/image.jpg",
        score = 8.5,
        year = 2023,
        synopsis = "Test synopsis",
        rating = "PG-13",
        duration = "2 hr",
        status = "Finished Airing",
        genres = listOf("Action", "Adventure"),
        studios = listOf("Test Studio")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovies should emit loading then success state`() = runTest {
        // Given
        val movies = listOf(testMovie)
        coEvery { repository.getAnimeMovies() } returns flowOf(
            Resource.Loading(),
            Resource.Success(movies)
        )

        // When
        viewModel = MovieListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(movies, state.movies)
            assertEquals("", state.error)
        }
    }

    @Test
    fun `loadMovies should emit loading then error state when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getAnimeMovies() } returns flowOf(
            Resource.Loading(),
            Resource.Error(errorMessage)
        )

        // When
        viewModel = MovieListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(emptyList<AnimeMovie>(), state.movies)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `loadMovies should show loading state`() = runTest {
        // Given
        coEvery { repository.getAnimeMovies() } returns flowOf(
            Resource.Loading()
        )

        // When
        viewModel = MovieListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(true, state.isLoading)
            assertEquals(emptyList<AnimeMovie>(), state.movies)
            assertEquals("", state.error)
        }
    }

    @Test
    fun `loadMovies should handle empty list`() = runTest {
        // Given
        coEvery { repository.getAnimeMovies() } returns flowOf(
            Resource.Loading(),
            Resource.Success(emptyList())
        )

        // When
        viewModel = MovieListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(emptyList<AnimeMovie>(), state.movies)
            assertEquals("", state.error)
        }
    }
}
