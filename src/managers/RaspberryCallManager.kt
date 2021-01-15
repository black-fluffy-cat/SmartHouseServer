package com.jj.smarthouseserver.managers

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.Logger

class RaspberryCallManager(private val logger: Logger) {

    companion object {
        private const val RASPBERRY_ADDRESS = "http://192.168.0.117:5000"
        private const val ALERT_PHOTO_ENDPOINT = "/alertPhoto"
        private const val ALERT_PHOTO_ADDRESS = RASPBERRY_ADDRESS + ALERT_PHOTO_ENDPOINT
    }

    private val httpClient = HttpClient(CIO)

    suspend fun pingRaspberryToMakePhoto() {
        val response = httpClient.post<HttpResponse> { url(ALERT_PHOTO_ADDRESS) /* body = */ }
        logger.info("After request to raspberry, resultCode: ${response.status}")
    }
}