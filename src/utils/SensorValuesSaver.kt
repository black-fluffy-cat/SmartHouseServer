package com.jj.smarthouseserver.utils

import java.io.*

class SensorValuesSaver {

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

    //TODO Remove duplicated code
    private fun createWriter(file: File): BufferedWriter? {
        try {
            return BufferedWriter(FileWriter(file, true))
        } catch (e: FileNotFoundException) {
            println("File not found, ${e.message}")
        }
        return null
    }

    @Synchronized
    private fun saveLog(tag: String?, message: String?, fileWriter: BufferedWriter) {
        try {
            fileWriter.append("${getDateStringWithMillis()} $tag: $message\n")
            fileWriter.flush()
        } catch (ioe: IOException) {
            println("Error while writing to file, ${ioe.message}")
        }
    }
}