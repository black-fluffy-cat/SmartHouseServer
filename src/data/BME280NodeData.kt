package com.jj.smarthouseserver.data

data class BME280NodeData(
    val deviceName: String,
    val temperatureC: String,
    val pressurehPa: String,
    val altitudeM: String
)