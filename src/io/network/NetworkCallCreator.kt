package com.jj.smarthouseserver.io.network

import com.jj.smarthouseserver.senders.LCDRowsData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class NetworkCallCreator : PingCreator {

    private val httpClient = HttpClient(CIO)

    override suspend fun get(urlWithEndpoint: String) = httpClient.get<HttpResponse>(urlWithEndpoint)

    override suspend fun post(urlWithEndpoint: String) = httpClient.post<HttpResponse>(urlWithEndpoint)

    override suspend fun post(url: String, lcdRowsData: LCDRowsData) =
        httpClient.post<HttpResponse>(url, body = lcdRowsData)
}