package com.jj.smarthouseserver.senders

import com.jj.smarthouseserver.io.network.PingCreator
import com.jj.smarthouseserver.managers.AlertState
import com.jj.smarthouseserver.managers.AlertStateManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import utils.coroutines.ICoroutineScopeProvider

class AlertVisualizerController(
    private val pingCreator: PingCreator,
    private val alertStateManager: AlertStateManager,
    private val coroutineScopeProvider: ICoroutineScopeProvider
) {

    companion object {
        private const val ALERT_ON_ENDPOINT = "/alert"
        private const val ALERT_OFF_ENDPOINT = "/alertOff"
    }

    private var nodeAddress = "http://192.168.1.6"

    init {
        coroutineScopeProvider.getIO().launch {
            alertStateManager.observeAlertState().collect { onAlertStateChanged(it) }
        }
    }

    private fun onAlertStateChanged(alertState: AlertState) {
        if (alertState.isAlertState) turnVisualizerOn()
        else turnVisualizerOff()
    }

    private fun turnVisualizerOn() = setVisualizer(ALERT_ON_ENDPOINT)

    private fun turnVisualizerOff() = setVisualizer(ALERT_OFF_ENDPOINT)

    private fun setVisualizer(endpoint: String) {
        val urlWithEndpoint = "$nodeAddress$endpoint"
        coroutineScopeProvider.getIO().launch { pingCreator.get(urlWithEndpoint) }
    }
}