package com.jj.smarthouseserver

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
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

data class HeartbeatData(val deviceName: String? = null, val timeFromStart: String, val timeFromAlert: String)
data class AlertData(val deviceName: String? = null, val timeFromStart: String, val alertState: String)

private lateinit var httpClient: HttpClient
private lateinit var raspberryCallManager: RaspberryCallManager

fun main(args: Array<String>) {

    httpClient = HttpClient(CIO)
    raspberryCallManager = RaspberryCallManager(httpClient, logger)

    val server = embeddedServer(Netty, 8080) {
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
        }
    }
    server.start(wait = true)
}
