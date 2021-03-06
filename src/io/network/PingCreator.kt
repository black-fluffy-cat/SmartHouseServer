package com.jj.smarthouseserver.io.network

import com.jj.smarthouseserver.senders.LCDRowsData
import io.ktor.client.statement.*

interface PingCreator {
    suspend fun get(urlWithEndpoint: String): HttpResponse
    suspend fun post(urlWithEndpoint: String): HttpResponse
    suspend fun post(url: String, lcdRowsData: LCDRowsData): HttpResponse
}