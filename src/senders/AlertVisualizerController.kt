package com.jj.smarthouseserver.senders

import com.jj.smarthouseserver.io.network.PingCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertVisualizerController(private val pingCreator: PingCreator) {

    companion object {
        private const val ALERT_ON_ENDPOINT = "/alert"
        private const val ALERT_OFF_ENDPOINT = "/alertOff"
    }

    private var nodeAddress = "http://192.168.1.6"

    fun turnVisualizerOn() {
        setVisualizer(ALERT_ON_ENDPOINT)
    }

    fun turnVisualizerOff() {
        setVisualizer(ALERT_OFF_ENDPOINT)
    }

    private fun setVisualizer(endpoint: String) {
        val urlWithEndpoint = "$nodeAddress$endpoint"
        CoroutineScope(Dispatchers.IO).launch {
            pingCreator.get(urlWithEndpoint)
        }
    }
}