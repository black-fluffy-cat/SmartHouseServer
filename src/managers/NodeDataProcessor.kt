package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.data.BME280NodeData
import com.jj.smarthouseserver.utils.LogSaver
import com.jj.smarthouseserver.utils.SensorValuesSaver
import com.jj.smarthouseserver.utils.tag
import data.SensorValues
import org.slf4j.Logger

class NodeDataProcessor(private val logger: Logger) {

    private val sensorValuesSaver = SensorValuesSaver()

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

    fun processSensorValues(sensorValues: SensorValues) {
        printLogAndSave("Received sensor values, size of list: ${sensorValues.listOfValues.size}")
        sensorValuesSaver.saveSensorValues(sensorValues.listOfValues)
    }
}