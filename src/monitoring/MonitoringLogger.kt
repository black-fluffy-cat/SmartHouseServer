package com.jj.smarthouseserver.monitoring

import com.jj.smarthouseserver.utils.LogSaver
import org.slf4j.Logger

class MonitoringLogger(private val logger: Logger) {

    companion object {
        private const val MONITORING_TAG = "Monitoring"
    }

    fun logUnavailableInfo(deviceId: String, timeDifference: Long) {
        logAndSaveInfo("Monitoring - Info - device $deviceId is unavailable for $timeDifference ms")
    }

    fun logAndSaveInfo(message: String) {
        logger.info(message)
        saveMonitoringLog(message)
    }

    fun logUnavailableAlert(deviceId: String, timeDifference: Long) {
        logAndSaveWarn("Monitoring - ALERT - device $deviceId is unavailable for $timeDifference ms")
    }

    private fun logAndSaveWarn(message: String) {
        logger.warn(message)
        saveMonitoringLog(message)
    }

    private fun saveMonitoringLog(message: String) {
        LogSaver.saveMonitoringLog(MONITORING_TAG, message)
    }
}