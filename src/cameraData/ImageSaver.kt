package com.jj.smarthouseserver.cameraData

import com.jj.smarthouseserver.utils.copyToSuspend
import io.ktor.http.content.*
import org.slf4j.Logger
import java.io.File

class ImageSaver(private val logger: Logger) {

    suspend fun savePartAsImage(part: PartData.FileItem, savePath: String) {
        val fileName = part.originalFileName
        val ext = File(fileName).extension
        val photoName = "${System.currentTimeMillis()}-$fileName.$ext"
        val file = File(savePath, photoName)
        part.streamProvider().use { input ->
            file.outputStream().buffered().use { output -> input.copyToSuspend(output) }
        }
        logger.info("receivePhoto - saved photo with name $photoName")
    }
}