package com.cacd2.cacdgame.datasource.api.lead

import com.cacd2.cacdgame.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.CurlUserAgent
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Created by francois.dabonot@cacd2.fr on 04/05/2023.
 */
object PipeDriveAPI {
    val httpClient =
        HttpClient {
            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                url("https://cacdgame.cacd2.io/")
                header(HttpHeaders.Authorization, "Bearer ${Constants.API_KEY}")
                header(HttpHeaders.ContentType, "application/json")
                header(HttpHeaders.Accept, "application/json")
            }
            CurlUserAgent()
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
            }
        }
}
