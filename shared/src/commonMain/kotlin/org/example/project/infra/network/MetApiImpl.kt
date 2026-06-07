package org.example.project.infra.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MetApiImpl(private val client: HttpClient) : MetApi {
    private val baseUrl = "https://collectionapi.metmuseum.org/public/collection/v1"

    override suspend fun searchArtworks(query: String): MetSearchResponse {
        return client.get("$baseUrl/search") {
            url {
                parameters.append("q", query)
            }
        }.body()
    }

    override suspend fun getArtworkDetail(objectId: Int): MetObjectResponse {
        return client.get("$baseUrl/objects/$objectId").body()
    }
}
