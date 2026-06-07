package org.example.project.infra.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.example.project.domain.model.Artwork
import org.example.project.infra.database.ArtworkDao
import org.example.project.infra.database.ArtworkLocalEntity
import org.example.project.infra.network.MetApi
import org.example.project.infra.network.MetObjectResponse
import org.example.project.infra.network.MetSearchResponse

class ArtworkRepositoryImplTest {

    private class MockMetApi : MetApi {
        var searchResponse: MetSearchResponse = MetSearchResponse(0, emptyList())
        var shouldFailSearch = false
        val detailsResponses = mutableMapOf<Int, MetObjectResponse>()
        val failedDetailsIds = mutableSetOf<Int>()

        override suspend fun searchArtworks(query: String): MetSearchResponse {
            if (shouldFailSearch) throw RuntimeException("Search failed")
            return searchResponse
        }

        override suspend fun getArtworkDetail(objectId: Int): MetObjectResponse {
            if (objectId in failedDetailsIds) throw RuntimeException("Detail failed")
            return detailsResponses[objectId] ?: MetObjectResponse(objectId)
        }
    }

    private class MockArtworkDao : ArtworkDao {
        val artworks = mutableMapOf<Int, ArtworkLocalEntity>()
        val favoriteFlow = MutableStateFlow<List<ArtworkLocalEntity>>(emptyList())
        var lastUpdatedFavoriteStatus: Pair<Int, Boolean>? = null
        var searchCacheResponse = mutableListOf<ArtworkLocalEntity>()

        override suspend fun getArtwork(objectId: Int): ArtworkLocalEntity? {
            return artworks[objectId]
        }

        override fun getFavoriteArtworks(): Flow<List<ArtworkLocalEntity>> {
            return favoriteFlow
        }

        override suspend fun insertArtwork(artwork: ArtworkLocalEntity) {
            artworks[artwork.objectId] = artwork
        }

        override suspend fun updateFavoriteStatus(objectId: Int, isFavorite: Boolean) {
            lastUpdatedFavoriteStatus = Pair(objectId, isFavorite)
            val current = artworks[objectId]
            if (current != null) {
                artworks[objectId] = current.copy(isFavorite = isFavorite)
            }
        }

        override suspend fun searchCachedArtworks(query: String): List<ArtworkLocalEntity> {
            return searchCacheResponse
        }
    }

    @Test
    fun testSearchArtworksSuccess() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao) { kotlinx.datetime.Instant.parse("2026-06-08T09:05:02Z") }

        api.searchResponse = MetSearchResponse(total = 2, objectIDs = listOf(101, 102))
        api.detailsResponses[101] = MetObjectResponse(101, "Monet Painting", "Claude Monet")
        api.detailsResponses[102] = MetObjectResponse(102, "Sunflowers", "Vincent van Gogh")

        val result = repository.searchArtworks("monet").getOrThrow()

        assertEquals(2, result.size)
        assertEquals(101, result[0].objectId)
        assertEquals("Monet Painting", result[0].title)
        assertEquals("Claude Monet", result[0].artistDisplayName)
        assertFalse(result[0].isFavorite)

        assertEquals(102, result[1].objectId)
        assertEquals("Sunflowers", result[1].title)
        assertEquals("Vincent van Gogh", result[1].artistDisplayName)
        assertFalse(result[1].isFavorite)

        // Verify inserted into database cache
        assertEquals(2, dao.artworks.size)
        assertEquals("Monet Painting", dao.artworks[101]?.title)
    }

    @Test
    fun testSearchArtworksNullIds() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        api.searchResponse = MetSearchResponse(total = 0, objectIDs = null)

        val result = repository.searchArtworks("empty").getOrThrow()
        assertTrue(result.isEmpty())
        assertTrue(dao.artworks.isEmpty())
    }

    @Test
    fun testSearchArtworksPreservesFavoriteStatus() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        // Already favorited in DB
        dao.artworks[101] = ArtworkLocalEntity(
            objectId = 101,
            title = "Cached Title",
            artistDisplayName = "Cached Artist",
            primaryImage = "",
            primaryImageSmall = "",
            department = "",
            objectDate = "",
            isFavorite = true,
            lastUpdated = "some-time"
        )

        api.searchResponse = MetSearchResponse(total = 1, objectIDs = listOf(101))
        api.detailsResponses[101] = MetObjectResponse(101, "Fresh Title", "Fresh Artist")

        val result = repository.searchArtworks("test").getOrThrow()
        assertEquals(1, result.size)
        assertTrue(result[0].isFavorite) // Must be favorited
        assertEquals("Fresh Title", result[0].title)
    }

    @Test
    fun testSearchArtworksMoreThanTenIds() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        api.searchResponse = MetSearchResponse(total = 12, objectIDs = (1..12).toList())
        val result = repository.searchArtworks("many").getOrThrow()
        assertEquals(10, result.size)
    }

    @Test
    fun testSearchArtworksCachedNotFavorite() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao) { kotlinx.datetime.Instant.parse("2026-10-12T15:35:45Z") }

        dao.artworks[101] = ArtworkLocalEntity(
            objectId = 101,
            title = "Cached Title",
            artistDisplayName = "Cached Artist",
            primaryImage = "",
            primaryImageSmall = "",
            department = "",
            objectDate = "",
            isFavorite = false,
            lastUpdated = "some-time"
        )

        api.searchResponse = MetSearchResponse(total = 1, objectIDs = listOf(101))
        api.detailsResponses[101] = MetObjectResponse(101, "Fresh Title", "Fresh Artist")

        val result = repository.searchArtworks("test").getOrThrow()
        assertEquals(1, result.size)
        assertFalse(result[0].isFavorite)
    }


    @Test
    fun testSearchArtworksDetailFetchFailureWithNoCache() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        api.searchResponse = MetSearchResponse(total = 1, objectIDs = listOf(101))
        api.failedDetailsIds.add(101)

        val result = repository.searchArtworks("test").getOrThrow()
        assertEquals(1, result.size)
        assertEquals(101, result[0].objectId)
        assertEquals("Failed to load detail", result[0].title)
    }

    @Test
    fun testSearchArtworksDetailFetchFailureWithExistingCache() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        dao.artworks[101] = ArtworkLocalEntity(
            objectId = 101,
            title = "Cached Title",
            artistDisplayName = "Cached Artist",
            primaryImage = "",
            primaryImageSmall = "",
            department = "",
            objectDate = "",
            isFavorite = true,
            lastUpdated = "some-time"
        )

        api.searchResponse = MetSearchResponse(total = 1, objectIDs = listOf(101))
        api.failedDetailsIds.add(101)

        val result = repository.searchArtworks("test").getOrThrow()
        assertEquals(1, result.size)
        assertEquals("Cached Title", result[0].title)
        assertTrue(result[0].isFavorite)
    }

    @Test
    fun testSearchArtworksTotalFailureWithCacheFallback() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        api.shouldFailSearch = true
        dao.searchCacheResponse.add(
            ArtworkLocalEntity(
                objectId = 101,
                title = "Cached Title",
                artistDisplayName = "Cached Artist",
                primaryImage = "",
                primaryImageSmall = "",
                department = "",
                objectDate = "",
                isFavorite = false,
                lastUpdated = "some-time"
            )
        )

        val result = repository.searchArtworks("test").getOrThrow()
        assertEquals(1, result.size)
        assertEquals("Cached Title", result[0].title)
    }

    @Test
    fun testSearchArtworksTotalFailureWithEmptyCacheThrows() {
        runBlocking {
            val api = MockMetApi()
            val dao = MockArtworkDao()
            val repository = ArtworkRepositoryImpl(api, dao)

            api.shouldFailSearch = true
            dao.searchCacheResponse = mutableListOf() // Empty cache

            assertFailsWith<RuntimeException> {
                repository.searchArtworks("test").getOrThrow()
            }
        }
    }

    @Test
    fun testObserveFavoriteArtworks() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        val favs = listOf(
            ArtworkLocalEntity(
                objectId = 101,
                title = "Fav Title",
                artistDisplayName = "Fav Artist",
                primaryImage = "",
                primaryImageSmall = "",
                department = "",
                objectDate = "",
                isFavorite = true,
                lastUpdated = "some-time"
            )
        )
        dao.favoriteFlow.value = favs

        val observed = repository.observeFavoriteArtworks().first()
        assertEquals(1, observed.size)
        assertEquals("Fav Title", observed[0].title)
    }

    @Test
    fun testToggleFavorite() = runBlocking {
        val api = MockMetApi()
        val dao = MockArtworkDao()
        val repository = ArtworkRepositoryImpl(api, dao)

        dao.artworks[101] = ArtworkLocalEntity(
            objectId = 101,
            title = "Title",
            artistDisplayName = "Artist",
            primaryImage = "",
            primaryImageSmall = "",
            department = "",
            objectDate = "",
            isFavorite = false,
            lastUpdated = "some-time"
        )

        repository.toggleFavorite(101, true)
        assertEquals(Pair(101, true), dao.lastUpdatedFavoriteStatus)
        assertTrue(dao.artworks[101]?.isFavorite == true)
    }
}
