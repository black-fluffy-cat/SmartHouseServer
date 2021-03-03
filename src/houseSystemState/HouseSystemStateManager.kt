package com.jj.smarthouseserver.houseSystemState

class HouseSystemStateManager {

    private val temperatureState = TemperatureState()
    private val houseState = HouseState(temperatureState)

    fun updateTemperatureState(dataSample: DataSample) {
        val currentTemperatures: HashMap<String, DataSample> = HashMap(temperatureState.temperatures)
        currentTemperatures[dataSample.deviceName] = dataSample

        temperatureState.temperatures = currentTemperatures.toMap()
    }

    fun getHouseSystemState(): HouseState = houseState.copy()
}