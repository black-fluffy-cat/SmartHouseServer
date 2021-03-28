package com.jj.smarthouseserver.houseSystemState

import com.jj.smarthouseserver.utils.coroutines.BufferedMutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class HouseSystemStateManager {

    private val temperatureState = TemperatureState()
    private val houseState = HouseState(temperatureState)

    private val houseSharedFlow = BufferedMutableSharedFlow<HouseState>()

    fun observeHouseSharedFlow(): SharedFlow<HouseState> = houseSharedFlow

    fun updateTemperatureState(dataSample: DataSample) {
        val currentTemperatures: HashMap<String, DataSample> = HashMap(temperatureState.temperatures)
        currentTemperatures[dataSample.deviceName] = dataSample

        temperatureState.temperatures = currentTemperatures.toMap()

        houseSharedFlow.tryEmit(houseState)
    }

    fun getHouseSystemState(): HouseState = houseState.copy()
}