package com.jj.smarthouseserver.senders

import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.managers.AlertState
import com.jj.smarthouseserver.managers.AlertStateManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import utils.coroutines.ICoroutineScopeProvider
import java.util.*

class LEDStripColorChanger(
    private val pingCreator: PingCreator,
    private val alertStateManager: AlertStateManager,
    private val coroutineScopeProvider: ICoroutineScopeProvider
) {

    private enum class AvailableLedColor {
        RED, GREEN, BLUE, YELLOW, WHITE, PURPLE, CYAN, RAINBOW
    }

    private var nodeAddress = "http://192.168.1.4"

    init {
        coroutineScopeProvider.getIO().launch {
            alertStateManager.observeAlertState().collect { onAlertStateChanged(it) }
        }
    }

    private fun onAlertStateChanged(alertState: AlertState) {
        if (alertState.isAlertState) setRedColor()
        else setGreenColor()
    }

    fun setLEDStripNodeIP(ip: String) {
        nodeAddress = "http://$ip"
    }

    fun setRedColor() = setColor(AvailableLedColor.RED)
    fun setGreenColor() = setColor(AvailableLedColor.GREEN)
    fun setBlueColor() = setColor(AvailableLedColor.BLUE)
    fun setYellowColor() = setColor(AvailableLedColor.YELLOW)
    fun setWhiteColor() = setColor(AvailableLedColor.WHITE)
    fun setPurpleColor() = setColor(AvailableLedColor.PURPLE)
    fun setCyanColor() = setColor(AvailableLedColor.CYAN)
    fun setRainbowColor() = setColor(AvailableLedColor.RAINBOW)

    private fun setColor(availableLedColor: AvailableLedColor) {
        val urlWithEndpoint = "$nodeAddress/${availableLedColor.toString().toLowerCase(Locale.ROOT)}"
        coroutineScopeProvider.getIO().launch { pingCreator.get(urlWithEndpoint) }
    }
}