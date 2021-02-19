package com.jj.smarthouseserver.io

import com.jj.smarthouseserver.utils.tag
import java.io.BufferedWriter
import java.io.File

class SensorValuesSaver : FileBridge() {

    companion object {
        private const val SENSOR_VALUES_FILE_NAME = "SENSOR_VALUES.txt"
    }

    private var sensorValuesWriter: BufferedWriter? = null
    private var sensorValuesFile: File? = null

    @Synchronized
    fun saveSensorValues(listOfValues: List<Triple<Float, Float, Float>>) {
        if (sensorValuesFile?.exists() != true || sensorValuesWriter == null) {
            sensorValuesFile = createFile(SENSOR_VALUES_FILE_NAME).also {
                sensorValuesWriter = createWriter(it)
            }
        }
        sensorValuesWriter?.let { writer ->
            listOfValues.forEach { values ->
                saveLog(tag, values.toString(), writer)
            }
        }
        println("Written sensor values to file, ${listOfValues.size}")
    }
}