package com.jj.smarthouseserver.senders

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertVisualizerController {

    companion object {
        private const val ALERT_ON_ENDPOINT = "/alert"
        private const val ALERT_OFF_ENDPOINT = "/alertOff"
    }

    private var nodeAddress = "http://192.168.1.6"

    private val httpClient = HttpClient(CIO)

    fun turnVisualizerOn() {
        setVisualizer(ALERT_ON_ENDPOINT)
    }

    fun turnVisualizerOff() {
        setVisualizer(ALERT_OFF_ENDPOINT)
    }

    private fun setVisualizer(endpoint: String) {
        val urlWithEndpoint = "$nodeAddress$endpoint"
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.get<HttpResponse>(urlWithEndpoint)
        }
    }
}