package com.jj.smarthouseserver.cameraData

import com.jj.smarthouseserver.utils.copyToSuspend
import io.ktor.http.content.*
import org.slf4j.Logger
import java.io.File

class FileSaver(private val logger: Logger) {

    suspend fun savePartFileToDisc(part: PartData.FileItem, savePath: String) {
        val fileName = part.originalFileName
        val ext = getFileExtension(fileName)
        val photoName = "${System.currentTimeMillis()}-$fileName.$ext"
        val file = File(savePath, photoName)
        part.streamProvider().use { input ->
            file.outputStream().buffered().use { output -> input.copyToSuspend(output) }
        }
        logger.info("savePartFileToDisc - saved file with name $photoName")
    }

    private fun getFileExtension(fileName: String?): String = fileName?.substringAfterLast('.', "") ?: ""
}