package com.jj.smarthouseserver.io.disc

import java.io.File
import kotlin.math.round

class FileCreator {
    fun createFile(path: String) = File(path)
}

class DiscSpaceMonitor(private val fileCreator: FileCreator = FileCreator()) {

    companion object {
        const val NUMBER_OF_RECORDINGS_TO_DELETE = 10
        const val ROOT_DIR_PATH = "/"
        const val DISC_USAGE_PERCENTAGE_THRESHOLD = 80.0
        const val VIDEOS_DIR_PATH = "/home/op/Desktop/SmartHouse/SmartHouseServer/receivedVideos"
    }

    fun onDiscOperationPerformed() {
        if (getDiscUsagePercentage() >= DISC_USAGE_PERCENTAGE_THRESHOLD) {
            deleteOldestRecordings()
        }
    }

    private fun getDiscUsagePercentage(): Double {
        val root = fileCreator.createFile(ROOT_DIR_PATH)
        val totalSpace = root.totalSpace.toDouble()
        val usableSpace = root.usableSpace.toDouble()
        return 100 - round(usableSpace / totalSpace * 10000) / 100
    }

    private fun deleteOldestRecordings(logDir: File = fileCreator.createFile(VIDEOS_DIR_PATH)) {
        val logFiles = logDir.listFiles()
        if (logFiles != null) {
            logFiles.sortBy { it.lastModified() }

            repeat(NUMBER_OF_RECORDINGS_TO_DELETE) { i -> logFiles.getOrNull(i)?.delete() }
        }
    }
}