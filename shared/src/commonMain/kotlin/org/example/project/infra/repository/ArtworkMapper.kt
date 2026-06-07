package org.example.project.infra.repository

import org.example.project.domain.model.Artwork
import org.example.project.infra.database.ArtworkLocalEntity
import org.example.project.infra.network.MetObjectResponse

object ArtworkMapper {
    fun toDomain(entity: ArtworkLocalEntity): Artwork {
        return Artwork(
            objectId = entity.objectId,
            title = entity.title,
            artistDisplayName = entity.artistDisplayName,
            primaryImage = entity.primaryImage,
            primaryImageSmall = entity.primaryImageSmall,
            department = entity.department,
            objectDate = entity.objectDate,
            isFavorite = entity.isFavorite,
            lastUpdated = entity.lastUpdated
        )
    }

    fun toLocalEntity(
        id: Int,
        detail: MetObjectResponse,
        isFavorite: Boolean,
        lastUpdated: String
    ): ArtworkLocalEntity {
        return ArtworkLocalEntity(
            objectId = id,
            title = detail.title ?: "Unknown",
            artistDisplayName = detail.artistDisplayName ?: "Unknown",
            primaryImage = detail.primaryImage ?: "",
            primaryImageSmall = detail.primaryImageSmall ?: "",
            department = detail.department ?: "Unknown",
            objectDate = detail.objectDate ?: "Unknown",
            isFavorite = isFavorite,
            lastUpdated = lastUpdated
        )
    }
}
