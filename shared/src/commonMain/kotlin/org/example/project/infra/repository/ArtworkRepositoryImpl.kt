package org.example.project.infra.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.model.Artwork
import org.example.project.domain.repository.ArtworkRepository
import org.example.project.infra.database.ArtworkDao
import org.example.project.infra.database.ArtworkLocalEntity
import org.example.project.infra.network.MetApi

class ArtworkRepositoryImpl(
    private val api: MetApi,
    private val dao: ArtworkDao,
    private val getCurrentInstant: () -> kotlinx.datetime.Instant = { Clock.System.now() }
) : ArtworkRepository {
    override suspend fun searchArtworks(query: String): Result<List<Artwork>> = runCatching {
        try {
            val searchResponse = api.searchArtworks(query)
            val objectIds = searchResponse.objectIDs
            val ids = if (objectIds != null) objectIds.take(10) else emptyList()
            
            val artworks = coroutineScope {
                ids.map { id ->
                    async {
                        val cached = dao.getArtwork(id)
                        val isFav = cached != null && cached.isFavorite
                        
                        try {
                            val detail = api.getArtworkDetail(id)
                            val formattedTime = getFormattedCurrentDateTimeInJapan()
                            val entity = ArtworkMapper.toLocalEntity(id, detail, isFav, formattedTime)
                            dao.insertArtwork(entity)
                            ArtworkMapper.toDomain(entity)
                        } catch (e: Exception) {
                            if (cached != null) {
                                ArtworkMapper.toDomain(cached)
                            } else {
                                Artwork(
                                    objectId = id,
                                    title = "Failed to load detail",
                                    artistDisplayName = "",
                                    primaryImage = "",
                                    primaryImageSmall = "",
                                    department = "",
                                    objectDate = "",
                                    isFavorite = isFav,
                                    lastUpdated = ""
                                )
                            }
                        }
                    }
                }.awaitAll()
            }
            artworks
        } catch (e: Exception) {
            val cachedList = dao.searchCachedArtworks(query)
            if (cachedList.isNotEmpty()) {
                cachedList.map { ArtworkMapper.toDomain(it) }
            } else {
                throw e
            }
        }
    }

    override fun observeFavoriteArtworks(): Flow<List<Artwork>> {
        return dao.getFavoriteArtworks().map { list ->
            list.map { ArtworkMapper.toDomain(it) }
        }
    }

    override suspend fun toggleFavorite(objectId: Int, isFavorite: Boolean) {
        dao.updateFavoriteStatus(objectId, isFavorite)
    }

    private fun getFormattedCurrentDateTimeInJapan(): String {
        val currentInstant = getCurrentInstant()
        val tokyoTimeZone = TimeZone.of("Asia/Tokyo")
        val localDateTime = currentInstant.toLocalDateTime(tokyoTimeZone)
        val year = localDateTime.year
        val month = padTwoDigits(localDateTime.month.ordinal + 1)
        val day = padTwoDigits(localDateTime.day)
        val hour = padTwoDigits(localDateTime.hour)
        val minute = padTwoDigits(localDateTime.minute)
        val second = padTwoDigits(localDateTime.second)
        return "$year/$month/$day $hour:$minute:$second"
    }

    private fun padTwoDigits(value: Int): String {
        return if (value < 10) "0$value" else value.toString()
    }
}

