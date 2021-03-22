package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.io.network.PingCreator
import org.slf4j.Logger

class RaspberryCallManager(private val logger: Logger, private val pingCreator: PingCreator) {

    companion object {
        private const val RASPBERRY_ADDRESS = "http://192.168.0.117:5000"
        private const val ALERT_PHOTO_ENDPOINT = "/alertPhoto"
        private const val ALERT_PHOTO_ADDRESS = RASPBERRY_ADDRESS + ALERT_PHOTO_ENDPOINT
    }

    suspend fun pingRaspberryToMakePhoto() {
        val response = pingCreator.post(ALERT_PHOTO_ADDRESS)
        logger.info("After request to raspberry, resultCode: ${response.status}")
    }
}