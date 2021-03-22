package com.jj.smarthouseserver.io.network

import io.ktor.client.statement.*

interface PingCreator {
    suspend fun get(urlWithEndpoint: String): HttpResponse
    suspend fun post(urlWithEndpoint: String): HttpResponse
}