package com.jj.smarthouseserver

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.Logger

class RaspberryCallManager(private val client: HttpClient, private val logger: Logger) {

    companion object {
        const val RASPBERRY_ADDRESS = "http://192.168.0.110:80"
        const val ALERT_PHOTO_ENDPOINT = "/alertPhoto"
        const val ALERT_PHOTO_ADDRESS = RASPBERRY_ADDRESS + ALERT_PHOTO_ENDPOINT
    }

    suspend fun pingRaspberryToMakePhoto() {
        val response = client.post<HttpResponse> {
            url(ALERT_PHOTO_ADDRESS)
//            body =
        }
        logger.info("After request to raspberry, resultCode: ${response.status}")
    }
}