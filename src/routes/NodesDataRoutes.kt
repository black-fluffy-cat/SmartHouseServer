package com.jj.smarthouseserver.routes

import com.jj.smarthouseserver.cameraData.ImageSaver
import com.jj.smarthouseserver.data.NgrokAddressesCallData
import com.jj.smarthouseserver.managers.NgrokAddressesProcessor
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger

fun Application.nodesDataRoutes() {
    val ngrokAddressesProcessor by inject(NgrokAddressesProcessor::class.java)
    val imageSaver by inject(ImageSaver::class.java)
    val logger by inject(Logger::class.java)
    routing {
        receivePhoto(imageSaver, logger)
        receiveNgrokAddresses(ngrokAddressesProcessor, logger)
    }
}

fun Route.receivePhoto(imageSaver: ImageSaver, logger: Logger) {
    post("/receivePhoto") {
        withContext(Dispatchers.IO) {
            logger.info("receivePhoto - received request")
            val savePath = "receivedPhotos/"

            call.receiveMultipart().forEachPart { part ->
                if (part is PartData.FileItem) imageSaver.savePartAsImage(part, savePath)
                part.dispose()
            }
            call.respond(mapOf("OK" to true))
        }
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