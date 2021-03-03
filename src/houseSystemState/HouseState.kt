package com.jj.smarthouseserver.houseSystemState

data class HouseState(val temperatureState: TemperatureState = TemperatureState())

data class TemperatureState(var temperatures: Map<String, DataSample> = mapOf())

data class DataSample(val deviceName: String, val value: String, val unit: String)