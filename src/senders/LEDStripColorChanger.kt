package com.jj.smarthouseserver.senders

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LEDStripColorChanger {

    private enum class AvailableLedColor {
        RED, GREEN, BLUE, YELLOW, WHITE, PURPLE, CYAN, RAINBOW
    }

    private var nodeAddress = "http://192.168.1.4"

    private val httpClient = HttpClient(CIO)

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
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.get<HttpResponse>(urlWithEndpoint)
        }
    }
}