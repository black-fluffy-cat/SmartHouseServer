package com.jj.smarthouseserver.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.atomic.AtomicBoolean

data class AlertArmSwitch(val alertArmed: Boolean)
data class AlertState(val isAlertState: Boolean)

class AlertStateManager {

    private val alertArmed = AtomicBoolean(true)

    private val alertState = MutableStateFlow(AlertState(false))
    fun observeAlertState(): StateFlow<AlertState> = alertState

    fun receiveAlert() {
        if (alertArmed.get()) alertState.value = AlertState(true)
    }

    fun receiveAlertOff() {
        if (alertArmed.get()) alertState.value = AlertState(false)
    }

    fun armAlert() = alertArmed.set(true)
    fun disarmAlert() = alertArmed.set(false)
}