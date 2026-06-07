package org.example.project.domain.usecase

import kotlinx.coroutines.runBlocking
import org.example.project.domain.model.Artwork
import org.example.project.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchArtworksUseCaseTest {

    private class FakeArtworkRepository : ArtworkRepository {
        var searchResult: Result<List<Artwork>> = Result.success(emptyList())
        var searchCallCount = 0
        var lastSearchQuery = ""

        override suspend fun searchArtworks(query: String): Result<List<Artwork>> {
            searchCallCount++
            lastSearchQuery = query
            return searchResult
        }

        override fun observeFavoriteArtworks(): Flow<List<Artwork>> = flowOf(emptyList())

        override suspend fun toggleFavorite(objectId: Int, isFavorite: Boolean) {}
    }

    @Test
    fun testSearchArtworksUseCase() = runBlocking {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)

        val artworks = listOf(
            Artwork(1, "Sunflowers", "Van Gogh", "", "", "Dept", "1888", false, "2026/06/08")
        )
        repo.searchResult = Result.success(artworks)

        val result = useCase("sunflowers")
        assertTrue(result.isSuccess)
        assertEquals(artworks, result.getOrThrow())
        assertEquals(1, repo.searchCallCount)
        assertEquals("sunflowers", repo.lastSearchQuery)
    }
}
