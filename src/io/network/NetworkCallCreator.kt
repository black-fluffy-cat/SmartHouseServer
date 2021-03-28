package com.jj.smarthouseserver.io.network

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.jj.smarthouseserver.senders.LCDRowsData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.jackson.*

class NetworkCallCreator : PingCreator {

    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override suspend fun get(urlWithEndpoint: String) = httpClient.get<HttpResponse>(urlWithEndpoint)

    override suspend fun post(urlWithEndpoint: String) = httpClient.post<HttpResponse>(urlWithEndpoint)

    override suspend fun post(url: String, lcdRowsData: LCDRowsData) =
        httpClient.post<HttpResponse> {
            body = lcdRowsData

            header(HttpHeaders.ContentType, ContentType.Application.Json)
            contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
            accept(ContentType.Application.Json)
            url(url)
        }
}