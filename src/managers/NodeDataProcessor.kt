package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.data.BME280NodeData
import com.jj.smarthouseserver.utils.LogSaver
import com.jj.smarthouseserver.utils.tag
import org.slf4j.Logger

class NodeDataProcessor(private val logger: Logger) {

    fun processBme280Data(bme280NodeData: BME280NodeData) {
        with(bme280NodeData) {
            printLogAndSave("Received BME280 data from id: $deviceName")
            printLogAndSave("temperatureC: $temperatureC, pressurehPa: $pressurehPa, altitudeM: $altitudeM")
        }
    }

    private fun printLogAndSave(message: String) {
        logger.info(message)
        LogSaver.saveNodeDataLog(this.tag, message)
    }
}