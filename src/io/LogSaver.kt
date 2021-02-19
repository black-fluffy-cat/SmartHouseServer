package com.jj.smarthouseserver.io

import java.io.BufferedWriter
import java.io.File

object LogSaver : FileBridge() {

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
}