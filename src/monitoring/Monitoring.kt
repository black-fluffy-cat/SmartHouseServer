package com.jj.smarthouseserver.monitoring

import com.jj.smarthouseserver.data.DeviceData
import com.jj.smarthouseserver.data.HeartbeatData
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

//TODO Make endpoints and methods for removing device from list by request
class Monitoring(logger: Logger) {

    companion object {
        private const val FIRST_ALERT_THRESHOLD = 60 * 1000L
        private const val SECOND_ALERT_THRESHOLD = 60 * 3 * 1000L
        private const val CHECKING_THREAD_DELAY = 30 * 1000L

        fun startMonitoring() {
            val monitoring by inject(Monitoring::class.java)
            monitoring.startMonitoring()
        }
    }

    private val seenDevices: ConcurrentLinkedQueue<DeviceData> = ConcurrentLinkedQueue()

    private val monitoringLogger = MonitoringLogger(logger)

    private val isCheckingThreadRunning = AtomicBoolean(false)
    private val checkingTask: Thread = Thread {
        while (true) {
            monitoringLogger.logAndSaveInfo("Monitoring - checkingTask started")
            val checkingTime = System.currentTimeMillis()
            seenDevices.forEach { device ->
                val timeDifference = checkingTime - device.lastTimeConnected
                when {
                    timeDifference > SECOND_ALERT_THRESHOLD ->
                        monitoringLogger.logUnavailableAlert(device.deviceId, timeDifference)
                    timeDifference > FIRST_ALERT_THRESHOLD ->
                        monitoringLogger.logUnavailableInfo(device.deviceId, timeDifference)
                }
            }
            Thread.sleep(CHECKING_THREAD_DELAY)
        }
    }

    /**
    @return true if successfully started checking thread, otherwise false
     */
    fun startMonitoring(): Boolean = if (isCheckingThreadRunning.compareAndSet(false, true)) {
        checkingTask.start()
        true
    } else false

    fun stopMonitoring(): Boolean = if (isCheckingThreadRunning.compareAndSet(true, false)) {
        checkingTask.interrupt()
        true
    } else false

    fun onReceivedHeartbeat(heartbeatData: HeartbeatData) {
        monitoringLogger.logAndSaveInfo("onReceivedHeartbeat from device: ${heartbeatData.deviceName}")
        heartbeatData.deviceName?.let { name ->
            onReceivedMessage(name, System.currentTimeMillis())
        }
    }

    private fun onReceivedMessage(deviceId: String, timeMillis: Long) {
        val device = seenDevices.find { it.deviceId == deviceId }
        if (device == null) {
            monitoringLogger.logAndSaveInfo("onReceivedMessage - new device connected, id: $deviceId")
            val seenDevice = DeviceData(deviceId, timeMillis)
            seenDevices.add(seenDevice)
        } else {
            monitoringLogger.logAndSaveInfo("onReceivedMessage - known device sent message, id: $deviceId")
            device.lastTimeConnected = timeMillis
        }
    }
}