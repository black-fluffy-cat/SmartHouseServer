package com.jj.smarthouseserver.monitoring

import com.jj.smarthouseserver.data.DeviceData
import com.jj.smarthouseserver.data.HeartbeatData
import org.slf4j.Logger
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

//TODO Make endpoints and methods for removing device from list by request
class Monitoring(private val logger: Logger) {

    companion object {
        private const val FIRST_ALERT_THRESHOLD = 60 * 1000L
        private const val SECOND_ALERT_THRESHOLD = 60 * 3 * 1000L
        private const val CHECKING_THREAD_DELAY = 30 * 1000L
    }

    private val seenDevices: ConcurrentLinkedQueue<DeviceData> = ConcurrentLinkedQueue()

    private val isCheckingThreadRunning = AtomicBoolean(false)
    private val checkingTask: Thread = Thread {
        while(true) {
            logger.info("Monitoring - checkingTask started")
            val checkingTime = System.currentTimeMillis()
            seenDevices.forEach { data ->
                val timeDifference = checkingTime - data.lastTimeConnected
                if (timeDifference > SECOND_ALERT_THRESHOLD) {
                    logger.warn("Monitoring - ALERT - device ${data.deviceId} is unavailable for $timeDifference ms")
                } else if (timeDifference > FIRST_ALERT_THRESHOLD) {
                    logger.warn("Monitoring - Info - device ${data.deviceId} is unavailable for $timeDifference ms")
                }
            }
            Thread.sleep(CHECKING_THREAD_DELAY)
        }
    }

    /**
    @return true if successfully started checking thread, otherwise false
     */
    fun startMonitoring(): Boolean {
        if (isCheckingThreadRunning.compareAndSet(false, true)) {
            checkingTask.start()
            return true
        }
        return false
    }

    fun stopMonitoring(): Boolean {
        if (isCheckingThreadRunning.compareAndSet(true, false)) {
            checkingTask.interrupt()
            return true
        }
        return false
    }

    fun onReceivedHeartbeat(heartbeatData: HeartbeatData) {
        logger.info("onReceivedHeartbeat from device: ${heartbeatData.deviceName}")
        heartbeatData.deviceName?.let { name ->
            onReceivedMessage(name, System.currentTimeMillis())
        }
    }

    private fun onReceivedMessage(deviceId: String, timeMillis: Long) {
        val device = seenDevices.find { it.deviceId == deviceId }
        if (device == null) {
            logger.info("onReceivedMessage - new device connected, id: $deviceId")
            val seenDevice = DeviceData(deviceId, timeMillis)
            seenDevices.add(seenDevice)
        } else {
            logger.info("onReceivedMessage - known device sent message, id: $deviceId")
            device.lastTimeConnected = timeMillis
        }
    }
}