package com.jj.smarthouseserver.data

//TODO Change to BMP - used sensor does not support reading humidity and is BMP280
data class BME280NodeData(
    val deviceName: String,
    val temperatureC: String,
    val pressurehPa: String,
    val altitudeM: String
)