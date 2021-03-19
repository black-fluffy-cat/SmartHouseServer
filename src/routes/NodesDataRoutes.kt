package com.jj.smarthouseserver.routes

import com.jj.smarthouseserver.cameraData.FileSaver
import com.jj.smarthouseserver.data.BME280NodeData
import com.jj.smarthouseserver.data.NgrokAddressesCallData
import com.jj.smarthouseserver.managers.NgrokAddressesProcessor
import com.jj.smarthouseserver.managers.NodeDataProcessor
import data.SensorValues
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
    val nodeDataProcessor by inject(NodeDataProcessor::class.java)
    val imageSaver by inject(FileSaver::class.java)
    val logger by inject(Logger::class.java)
    routing {
        receivePhoto(imageSaver, logger)
        receiveVideo(imageSaver, logger)
        receiveNgrokAddresses(ngrokAddressesProcessor, logger)
        receiveBme280Data(nodeDataProcessor, logger)
        receiveSensorValues(nodeDataProcessor, logger)
        receivePIRAlert(nodeDataProcessor, logger)
    }
}

fun Route.receivePhoto(fileSaver: FileSaver, logger: Logger) {
    post("/receivePhoto") {
        withContext(Dispatchers.IO) {
            logger.info("receivePhoto - received request")
            val savePath = "receivedPhotos/"

            call.receiveMultipart().forEachPart { part ->
                if (part is PartData.FileItem) fileSaver.savePartFileToDisc(part, savePath)
                part.dispose()
            }
            call.respond(mapOf("OK" to true))
        }
    }
}

fun Route.receiveVideo(fileSaver: FileSaver, logger: Logger) {
    post("/receiveVideo") {
        withContext(Dispatchers.IO) {
            logger.info("receiveVideo - received request")
            val savePath = "receivedVideos/"

            call.receiveMultipart().forEachPart { part ->
                if (part is PartData.FileItem) fileSaver.savePartFileToDisc(part, savePath)
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

fun Route.receiveBme280Data(nodeDataProcessor: NodeDataProcessor, logger: Logger) {
    post("/bme280Data") {
        withContext(Dispatchers.IO) {
            logger.info("receiveBme280Data - received request")
            with(call.receive<BME280NodeData>()) { nodeDataProcessor.processBme280Data(this) }
            call.respond(mapOf("OK" to true))
        }
    }
}

fun Route.receiveSensorValues(nodeDataProcessor: NodeDataProcessor, logger: Logger) {
    post("sensor/postValues") {
        withContext(Dispatchers.IO) {
            try {
                logger.info("receiveSensorValues - received request")
                with(call.receive<SensorValues>()) { nodeDataProcessor.processSensorValues(this) }
                call.respond(mapOf("OK" to true))
            } catch (e: Exception) {
                logger.error("Exception", e)
            }
        }
    }
}

fun Route.receivePIRAlert(nodeDataProcessor: NodeDataProcessor, logger: Logger) {
    post("/pirAlert") {
        withContext(Dispatchers.IO) {
            logger.info("receivePIRAlert - received ALERT")
            nodeDataProcessor.processPirAlert()
            call.respond(mapOf("OK" to true))
        }
    }
}