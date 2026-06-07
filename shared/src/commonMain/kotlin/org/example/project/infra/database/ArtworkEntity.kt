package org.example.project.infra.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "artworks")
data class ArtworkLocalEntity(
    @PrimaryKey val objectId: Int,
    val title: String,
    val artistDisplayName: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val department: String,
    val objectDate: String,
    val isFavorite: Boolean = false,
    val lastUpdated: String
)

@Dao
interface ArtworkDao {
    @Query("SELECT * FROM artworks WHERE objectId = :objectId")
    suspend fun getArtwork(objectId: Int): ArtworkLocalEntity?

    @Query("SELECT * FROM artworks WHERE isFavorite = 1")
    fun getFavoriteArtworks(): Flow<List<ArtworkLocalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artwork: ArtworkLocalEntity)

    @Query("UPDATE artworks SET isFavorite = :isFavorite WHERE objectId = :objectId")
    suspend fun updateFavoriteStatus(objectId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM artworks WHERE title LIKE '%' || :query || '%' OR artistDisplayName LIKE '%' || :query || '%'")
    suspend fun searchCachedArtworks(query: String): List<ArtworkLocalEntity>
}
