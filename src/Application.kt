package com.jj.smarthouseserver

import com.jj.smarthouseserver.data.*
import com.jj.smarthouseserver.managers.NgrokAddressesProcessor
import com.jj.smarthouseserver.managers.RaspberryCallManager
import com.jj.smarthouseserver.monitoring.Monitoring
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

private val logger: Logger = LoggerFactory.getLogger("MainLogger")
private const val SERVER_PORT = 8080

private val raspberryCallManager = RaspberryCallManager(logger)
private val ngrokAddressesProcessor = NgrokAddressesProcessor(logger)
private val monitoring = Monitoring(logger)

fun main(args: Array<String>) {

    val server = embeddedServer(Netty, SERVER_PORT) {
        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter())
        }
        routing {
            post("/alert") {
                withContext(Dispatchers.IO) {
                    val alertData = call.receive<AlertData>()
                    logger.info(
                        "Alert - deviceName: ${alertData.deviceName}, timeFromStart: ${alertData.timeFromStart}," +
                                " alertState: ${alertData.alertState}"
                    )
                    call.respond(mapOf("OK" to true))

                    raspberryCallManager.pingRaspberryToMakePhoto()
                }
            }
            post("/alertFeedback") {
                withContext(Dispatchers.IO) {
                    val alertFeedbackData = call.receive<AlertFeedbackData>()
                    logger.info(
                        "AlertFeedback - deviceName: ${alertFeedbackData.deviceName}, timeFromStart: ${alertFeedbackData.timeFromStart}," +
                                " additionalInfo: ${alertFeedbackData.additionalInfo}"
                    )
                    call.respond(mapOf("OK" to true))
                }
            }
            post("/heartbeat") {
                withContext(Dispatchers.IO) {
                    val heartbeatData = call.receive<HeartbeatData>()
                    logger.info(
                        "Heartbeat - deviceName: ${heartbeatData.deviceName}, timeFromStart: ${heartbeatData.timeFromStart}," +
                                " timeFromAlert: ${heartbeatData.timeFromAlert}"
                    )
                    monitoring.onReceivedHeartbeat(heartbeatData)
                    call.respond(mapOf("OK" to true))
                }
            }
            post("/raspPhoto") {
                withContext(Dispatchers.IO) {
                    raspberryCallManager.pingRaspberryToMakePhoto()
                    call.respond(mapOf("OK" to true))
                }
            }
            post("/receivePhoto") {
                withContext(Dispatchers.IO) {
                    logger.info("receivePhoto - received request")
                    try {
                        val multipart = call.receiveMultipart()
                        val saveDir = "receivedPhotos/"

                        multipart.forEachPart { part ->
                            when (part) {
                                is PartData.FileItem -> {
                                    val fileName = part.originalFileName
                                    val ext = File(fileName).extension
                                    val photoName = "$fileName-${System.currentTimeMillis()}.$ext"
                                    val file = File(saveDir, photoName)
                                    part.streamProvider().use { input ->
                                        file.outputStream().buffered().use { output ->
                                            input.copyToSuspend(output)
                                        }
                                    }
                                    logger.info("receivePhoto - saved photo with name $photoName")
                                }
                            }
                            part.dispose()
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }

            post("/receiveNgrokAddresses") {
                withContext(Dispatchers.IO) {
                    try {
                        logger.info("receiveNgrokAddresses - received request")
                        val ngrokAddressesCallData = call.receive<NgrokAddressesCallData>()
                        ngrokAddressesProcessor.process(ngrokAddressesCallData)
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }

            get("/raspPhoto") {
                withContext(Dispatchers.IO) {
                    raspberryCallManager.pingRaspberryToMakePhoto()
                    call.respond(mapOf("OK" to true))
                }
            }
            get("/health") {
                withContext(Dispatchers.IO) {
                    call.respond("I am running!")
                }
            }
        }
    }
    server.start(wait = true)
    monitoring.startMonitoring()
}
