package org.example.project.infra.network

import kotlinx.serialization.Serializable

@Serializable
data class MetSearchResponse(
    val total: Int,
    val objectIDs: List<Int>? = null
)

@Serializable
data class MetObjectResponse(
    val objectID: Int,
    val title: String? = null,
    val artistDisplayName: String? = null,
    val primaryImage: String? = null,
    val primaryImageSmall: String? = null,
    val department: String? = null,
    val objectDate: String? = null
)

interface MetApi {
    suspend fun searchArtworks(query: String): MetSearchResponse
    suspend fun getArtworkDetail(objectId: Int): MetObjectResponse
}
