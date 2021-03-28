package com.jj.smarthouseserver.senders

import com.jj.smarthouseserver.houseSystemState.HouseState
import com.jj.smarthouseserver.houseSystemState.HouseSystemStateManager
import com.jj.smarthouseserver.io.network.PingCreator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import utils.coroutines.ICoroutineScopeProvider
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@kotlinx.serialization.Serializable
data class LCDRowsData(
    val firstRow: String, val secondRow: String,
    val thirdRow: String, val fourthRow: String
) : Serializable

class LCDStateVisualizerController(
    private val houseSystemStateManager: HouseSystemStateManager,
    private val networkCallCreator: PingCreator,
    private val coroutineScopeProvider: ICoroutineScopeProvider
) {

    companion object {
        private const val SEND_HOUSE_STATE_ENDPOINT = "/setText"
    }

    init {
        coroutineScopeProvider.getIO().launch {
            houseSystemStateManager.observeHouseSharedFlow().collect { onHouseSystemStateChanged(it) }
        }
    }

    private var nodeAddress = "http://192.168.1.6"

    fun setLCDNodeIP(ip: String) {
        nodeAddress = "http://$ip"
    }

    private fun onHouseSystemStateChanged(houseState: HouseState) {
        val urlWithEndpoint = "$nodeAddress$SEND_HOUSE_STATE_ENDPOINT"
        val lcdData = prepareLCDData(houseState)
        coroutineScopeProvider.getIO().launch { networkCallCreator.post(urlWithEndpoint, lcdData) }
        print("onHouseSystemStateChanged, houseState: $houseState")
    }

    private fun prepareLCDData(houseState: HouseState): LCDRowsData {
        val temperaturesList = houseState.temperatureState.temperatures.toList()
        val time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME)
        val firstRow = "Dane z: $time"
        val secondRow = ""

        val firstDataSample = temperaturesList.getOrNull(0)?.second
        val thirdRow = "Dwor: ${firstDataSample?.value ?: ""} ${firstDataSample?.unit ?: ""}"

        val secondDataSample = temperaturesList.getOrNull(1)?.second
        val fourthRow = "Dom: ${secondDataSample?.value ?: ""} ${secondDataSample?.unit ?: ""}"

        return LCDRowsData(firstRow, secondRow, thirdRow, fourthRow)
    }
}