package com.example.movies.data.repository

import app.cash.turbine.test
import com.example.movies.data.model.*
import com.example.movies.data.remote.JikanApiService
import com.example.movies.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AnimeRepositoryImplTest {

    private lateinit var api: JikanApiService
    private lateinit var repository: AnimeRepositoryImpl

    private val testAnimeData = AnimeData(
        malId = 1,
        url = "https://test.com",
        images = Images(
            jpg = ImageUrl("https://test.com/image.jpg", null, null),
            webp = ImageUrl("https://test.com/image.webp", null, null)
        ),
        title = "Test Movie",
        titleEnglish = "Test Movie",
        titleJapanese = "テスト映画",
        type = "Movie",
        episodes = 1,
        status = "Finished Airing",
        aired = Aired("2023-01-01", "2023-01-01", "Jan 1, 2023"),
        duration = "2 hr",
        rating = "PG-13",
        score = 8.5,
        scoredBy = 1000,
        rank = 100,
        popularity = 500,
        synopsis = "Test synopsis",
        background = null,
        year = 2023,
        genres = listOf(
            Genre(1, "anime", "Action", "https://test.com/action")
        ),
        studios = listOf(
            Studio(1, "anime", "Test Studio", "https://test.com/studio")
        )
    )

    private val testAnimeResponse = AnimeResponse(
        data = listOf(testAnimeData),
        pagination = Pagination(1, false)
    )

    @Before
    fun setup() {
        api = mockk()
        repository = AnimeRepositoryImpl(api)
    }

    @Test
    fun `getAnimeMovies should emit Loading then Success`() = runTest {
        // Given
        coEvery { api.getAnimeMovies(any(), any(), any()) } returns testAnimeResponse

        // When
        repository.getAnimeMovies().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(1, successState.data?.size)
            assertEquals("Test Movie", successState.data?.first()?.title)
            assertEquals(8.5, successState.data?.first()?.score)

            awaitComplete()
        }
    }

    @Test
    fun `getAnimeMovies should emit Loading then Error on HttpException`() = runTest {
        // Given
        val httpException = HttpException(
            Response.error<AnimeResponse>(404, "Not Found".toResponseBody())
        )
        coEvery { api.getAnimeMovies(any(), any(), any()) } throws httpException

        // When
        repository.getAnimeMovies().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertTrue(errorState.message?.isNotEmpty() == true)

            awaitComplete()
        }
    }

    @Test
    fun `getAnimeMovies should emit Loading then Error on IOException`() = runTest {
        // Given
        coEvery { api.getAnimeMovies(any(), any(), any()) } throws IOException("Network error")

        // When
        repository.getAnimeMovies().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals("Couldn't reach server. Check your internet connection.", errorState.message)

            awaitComplete()
        }
    }

    @Test
    fun `getAnimeMovies should emit Loading then Error on generic Exception`() = runTest {
        // Given
        coEvery { api.getAnimeMovies(any(), any(), any()) } throws RuntimeException("Unknown error")

        // When
        repository.getAnimeMovies().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertTrue(errorState.message?.isNotEmpty() == true)

            awaitComplete()
        }
    }

    @Test
    fun `getAnimeMovies should handle empty response`() = runTest {
        // Given
        val emptyResponse = AnimeResponse(
            data = emptyList(),
            pagination = Pagination(1, false)
        )
        coEvery { api.getAnimeMovies(any(), any(), any()) } returns emptyResponse

        // When
        repository.getAnimeMovies().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(0, successState.data?.size)

            awaitComplete()
        }
    }

    @Test
    fun `getAnimeMovies should map data correctly to domain model`() = runTest {
        // Given
        coEvery { api.getAnimeMovies(any(), any(), any()) } returns testAnimeResponse

        // When
        repository.getAnimeMovies().test {
            // Then
            awaitItem() // Skip loading

            val successState = awaitItem()
            val movie = successState.data?.first()

            assertEquals(1, movie?.id)
            assertEquals("Test Movie", movie?.title)
            assertEquals(8.5, movie?.score)
            assertEquals(2023, movie?.year)
            assertEquals("Test synopsis", movie?.synopsis)
            assertEquals("PG-13", movie?.rating)
            assertEquals("2 hr", movie?.duration)
            assertEquals("Finished Airing", movie?.status)
            assertEquals(listOf("Action"), movie?.genres)
            assertEquals(listOf("Test Studio"), movie?.studios)

            awaitComplete()
        }
    }
}
