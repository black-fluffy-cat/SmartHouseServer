package com.jj.smarthouseserver.routes

import com.jj.smarthouseserver.data.NgrokAddressesCallData
import com.jj.smarthouseserver.managers.NgrokAddressesProcessor
import com.jj.smarthouseserver.utils.copyToSuspend
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import java.io.File

fun Application.nodesDataRoutes() {
    val ngrokAddressesProcessor by inject(NgrokAddressesProcessor::class.java)
    val logger by inject(Logger::class.java)
    routing {
        receivePhoto(logger)
        receiveNgrokAddresses(ngrokAddressesProcessor, logger)
    }
}

fun Route.receivePhoto(logger: Logger) {
    post("/receivePhoto") {
        withContext(Dispatchers.IO) {
            logger.info("receivePhoto - received request")
            val multipart = call.receiveMultipart()
            val saveDir = "receivedPhotos/"

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val fileName = part.originalFileName
                        val ext = File(fileName).extension
                        val photoName = "${System.currentTimeMillis()}-$fileName.$ext"
                        val file = File(saveDir, photoName)
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyToSuspend(output)
                            }
                        }
                        logger.info("receivePhoto - saved photo with name $photoName")
                    }
                    else -> { /* noop */
                    }
                }
                part.dispose()
            }
        }
        call.respond(mapOf("OK" to true))
    }
}

fun Route.receiveNgrokAddresses(ngrokAddressesProcessor: NgrokAddressesProcessor, logger: Logger) {
    post("/receiveNgrokAddresses") {
        withContext(Dispatchers.IO) {
            logger.info("receiveNgrokAddresses - received request")
            with(call.receive<NgrokAddressesCallData>()) { ngrokAddressesProcessor.process(this) }
            call.respond(mapOf("OK" to true))
        }
    }
}