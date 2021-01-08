package com.jj.smarthouseserver.utils

import java.io.*

object LogSaver {

    private const val LOGS_FILE_NAME = "LOGS_FILE.txt"

    private var bufferedFileWriter: BufferedWriter? = null
    private var currentFile: File? = null

    private fun createWriter() {
        try {
            val file = File(LOGS_FILE_NAME)
            bufferedFileWriter = BufferedWriter(FileWriter(file, true))
        } catch (e: FileNotFoundException) {
            println("File not found, ${e.message}")
        }
    }

    @Synchronized
    fun saveLog(tag: String?, message: String?) {
        if (currentFile?.exists() != true || bufferedFileWriter == null) createWriter()
        try {
            bufferedFileWriter?.append("${getDateStringWithMillis()} $tag: $message\n")
            bufferedFileWriter?.flush()
        } catch (ioe: IOException) {
            println("Error while writing to file, ${ioe.message}")
        }
    }
}