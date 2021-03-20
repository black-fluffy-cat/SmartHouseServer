package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.data.BME280NodeData
import com.jj.smarthouseserver.houseSystemState.DataSample
import com.jj.smarthouseserver.houseSystemState.HouseSystemStateManager
import com.jj.smarthouseserver.io.LogSaver
import com.jj.smarthouseserver.io.SensorValuesSaver
import com.jj.smarthouseserver.senders.LEDStripColorChanger
import com.jj.smarthouseserver.utils.tag
import data.SensorValues
import org.slf4j.Logger

class NodeDataProcessor(
    private val logger: Logger,
    private val houseSystemStateManager: HouseSystemStateManager,
    private val alertStateManager: AlertStateManager,
    private val ledStripColorChanger: LEDStripColorChanger
) {

    private val sensorValuesSaver = SensorValuesSaver()

    fun processBme280Data(bme280NodeData: BME280NodeData) {
        with(bme280NodeData) {
            printLogAndSave("Received BME280 data from id: $deviceName")
            printLogAndSave("temperatureC: $temperatureC, pressurehPa: $pressurehPa, altitudeM: $altitudeM")

            houseSystemStateManager.updateTemperatureState(DataSample(deviceName, temperatureC, "C"))
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

    fun processPirAlert() {
        printLogAndSave("Received PIR alert request")
        alertStateManager.receiveAlert()
    }

    fun processPirAlertOff() {
        printLogAndSave("Received PIR alertOff request")
        alertStateManager.receiveAlertOff()
    }

    fun setLEDStripNodeIP(ip: String) {
        printLogAndSave("Received set nodeIP request")
        ledStripColorChanger.setLEDStripNodeIP(ip)
    }

    fun alertArmSwitch(alertArmSwitch: AlertArmSwitch) {
        if (alertArmSwitch.alertArmed) alertStateManager.armAlert()
        else alertStateManager.disarmAlert()
    }
}