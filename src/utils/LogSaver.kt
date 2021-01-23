package com.jj.smarthouseserver.utils

import java.io.*

object LogSaver {

    private const val NODE_DATA_LOGS_FILE_NAME = "NODE_DATA_LOGS.txt"
    private const val NGROK_LOGS_FILE_NAME = "NGROK_LOGS.txt"
    private const val MONITORING_LOGS_FILE_NAME = "MONITORING_LOGS.txt"

    private var nodeDataLogsWriter: BufferedWriter? = null
    private var nodeDataLogsFile: File? = null

    private var ngrokLogsWriter: BufferedWriter? = null
    private var ngrokLogsFile: File? = null

    private var monitoringLogsWriter: BufferedWriter? = null
    private var monitoringLogsFile: File? = null

    fun saveNodeDataLog(tag: String?, message: String?) {
        if (nodeDataLogsFile?.exists() != true || nodeDataLogsWriter == null) {
            nodeDataLogsFile = createFile(NODE_DATA_LOGS_FILE_NAME).also {
                nodeDataLogsWriter = createWriter(it)
            }
        }
        nodeDataLogsWriter?.let { writer -> saveLog(tag, message, writer) }
    }

    fun saveNgrokLog(tag: String?, message: String?) {
        if (ngrokLogsFile?.exists() != true || ngrokLogsWriter == null) {
            ngrokLogsFile = createFile(NGROK_LOGS_FILE_NAME).also {
                ngrokLogsWriter = createWriter(it)
            }
        }
        ngrokLogsWriter?.let { writer -> saveLog(tag, message, writer) }
    }

    fun saveMonitoringLog(tag: String?, message: String?) {
        if (monitoringLogsFile?.exists() != true || monitoringLogsWriter == null) {
            monitoringLogsFile = createFile(MONITORING_LOGS_FILE_NAME).also {
                monitoringLogsWriter = createWriter(it)
            }
        }
        monitoringLogsWriter?.let { writer -> saveLog(tag, message, writer) }
    }

    private fun createWriter(file: File): BufferedWriter? {
        try {
            return BufferedWriter(FileWriter(file, true))
        } catch (e: FileNotFoundException) {
            println("File not found, ${e.message}")
        }
        return null
    }

    private fun createFile(fileName: String) = File(fileName)

    @Synchronized
    private fun saveLog(tag: String?, message: String?, fileWriter: BufferedWriter) {
        try {
            fileWriter.append("${getDateStringWithMillis()} $tag: $message\n")
            fileWriter.flush()
        } catch (ioe: IOException) {
            println("Error while writing to file, ${ioe.message}")
        }
    }
}