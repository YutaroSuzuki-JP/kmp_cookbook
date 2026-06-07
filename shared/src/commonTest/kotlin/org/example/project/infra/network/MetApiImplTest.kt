package org.example.project.infra.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class MetApiImplTest {

    @Test
    fun testSearchArtworks() = runBlocking {
        val mockEngine = MockEngine { request ->
            assertEquals("https://collectionapi.metmuseum.org/public/collection/v1/search?q=sunflowers", request.url.toString())
            respond(
                content = """{"total": 2, "objectIDs": [1, 2]}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val api = MetApiImpl(client)
        val response = api.searchArtworks("sunflowers")
        assertEquals(2, response.total)
        assertEquals(listOf(1, 2), response.objectIDs)
    }

    @Test
    fun testGetArtworkDetail() = runBlocking {
        val mockEngine = MockEngine { request ->
            assertEquals("https://collectionapi.metmuseum.org/public/collection/v1/objects/123", request.url.toString())
            respond(
                content = """
                    {
                        "objectID": 123,
                        "title": "Sunflowers",
                        "artistDisplayName": "Vincent van Gogh",
                        "primaryImage": "image_url",
                        "primaryImageSmall": "small_url",
                        "department": "Paintings",
                        "objectDate": "1888"
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val api = MetApiImpl(client)
        val response = api.getArtworkDetail(123)
        assertEquals(123, response.objectID)
        assertEquals("Sunflowers", response.title)
        assertEquals("Vincent van Gogh", response.artistDisplayName)
        assertEquals("image_url", response.primaryImage)
        assertEquals("small_url", response.primaryImageSmall)
        assertEquals("Paintings", response.department)
        assertEquals("1888", response.objectDate)
    }
}
