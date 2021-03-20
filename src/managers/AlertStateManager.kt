package com.jj.smarthouseserver.managers

import com.jj.smarthouseserver.senders.LEDStripColorChanger
import java.util.concurrent.atomic.AtomicBoolean

data class AlertArmSwitch(val alertArmed: Boolean)

class AlertStateManager(private val ledStripColorChanger: LEDStripColorChanger) {

    private val alertArmed = AtomicBoolean(true)

    fun receiveAlert() {
        if (alertArmed.get()) {
            setRedLeds()
            // TODO Send data to buzzer node
        }
    }

    fun receiveAlertOff() {
        if (alertArmed.get()) {
            setGreenLeds()
        }
    }

    fun armAlert() = alertArmed.set(true)
    fun disarmAlert() = alertArmed.set(false)

    private fun setRedLeds() = ledStripColorChanger.setRedColor()

    private fun setGreenLeds() = ledStripColorChanger.setGreenColor()
}