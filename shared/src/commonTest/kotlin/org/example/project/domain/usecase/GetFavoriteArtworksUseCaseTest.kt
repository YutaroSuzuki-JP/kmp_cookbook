package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.example.project.domain.model.Artwork
import org.example.project.domain.repository.ArtworkRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFavoriteArtworksUseCaseTest {

    private class FakeArtworkRepository : ArtworkRepository {
        val favoriteArtworks = listOf(
            Artwork(1, "Sunflowers", "Van Gogh", "", "", "Dept", "1888", true, "2026/06/08")
        )
        override suspend fun searchArtworks(query: String): Result<List<Artwork>> = Result.success(emptyList())

        override fun observeFavoriteArtworks(): Flow<List<Artwork>> {
            return flowOf(favoriteArtworks)
        }

        override suspend fun toggleFavorite(objectId: Int, isFavorite: Boolean) {}
    }

    @Test
    fun testGetFavoriteArtworksUseCase() = runBlocking {
        val repo = FakeArtworkRepository()
        val useCase = GetFavoriteArtworksUseCase(repo)

        val result = useCase().first()
        assertEquals(repo.favoriteArtworks, result)
    }
}
