package com.jj.smarthouseserver.io

import com.jj.smarthouseserver.utils.getDateStringWithMillis
import java.io.*

abstract class FileBridge {

    fun createFile(fileName: String) = File(fileName)

    protected fun createWriter(file: File): BufferedWriter? {
        try {
            return BufferedWriter(FileWriter(file, true))
        } catch (e: FileNotFoundException) {
            println("File not found, ${e.message}")
        }
        return null
    }

    @Synchronized
    protected fun saveLog(tag: String?, message: String?, fileWriter: BufferedWriter) {
        try {
            fileWriter.append("${getDateStringWithMillis()} $tag: $message\n")
            fileWriter.flush()
        } catch (ioe: IOException) {
            println("Error while writing to file, ${ioe.message}")
        }
    }
}