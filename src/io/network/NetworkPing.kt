package com.jj.smarthouseserver.io.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class NetworkPing : PingCreator {

    private val httpClient = HttpClient(CIO)

    override suspend fun get(urlWithEndpoint: String) = httpClient.get<HttpResponse>(urlWithEndpoint)

    override suspend fun post(urlWithEndpoint: String) = httpClient.post<HttpResponse>(urlWithEndpoint)
}