package com.jj.smarthouseserver

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("MainLogger")

data class HeartbeatData(val deviceName: String? = null, val timeFromStart: String, val timeFromAlert: String)
data class AlertData(val deviceName: String? = null, val timeFromStart: String, val alertState: String)

fun main(args: Array<String>) {
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
