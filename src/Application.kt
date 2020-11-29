package com.jj.smarthouseserver

import com.jj.smarthouseserver.data.AlertData
import com.jj.smarthouseserver.data.AlertFeedbackData
import com.jj.smarthouseserver.data.HeartbeatData
import com.jj.smarthouseserver.managers.RaspberryCallManager
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
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

private val logger: Logger = LoggerFactory.getLogger("MainLogger")
private const val SERVER_PORT = 8080

private val raspberryCallManager = RaspberryCallManager(logger)

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
                    call.respond(mapOf("OK" to true))
                }
            }
            post("/raspPhoto") {
                withContext(Dispatchers.IO) {
                    raspberryCallManager.pingRaspberryToMakePhoto()
                    call.respond(mapOf("OK" to true))
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
}
