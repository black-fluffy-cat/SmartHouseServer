package com.jj.smarthouseserver.routes

import com.jj.smarthouseserver.data.AlertData
import com.jj.smarthouseserver.data.HeartbeatData
import com.jj.smarthouseserver.managers.RaspberryCallManager
import com.jj.smarthouseserver.monitoring.Monitoring
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger

fun Application.monitoringRoutes() {
    val raspberryCallManager by inject(RaspberryCallManager::class.java)
    val monitoring by inject(Monitoring::class.java)
    val logger by inject(Logger::class.java)

    routing {
        receiveNodeAlertRoute(raspberryCallManager, logger)
        receiveHeartbeatRoute(monitoring, logger)
    }
}

fun Route.receiveNodeAlertRoute(raspberryCallManager: RaspberryCallManager, logger: Logger) {
    post("/alert") {
        with(call.receive<AlertData>()) {
            logger.info("Alert - deviceName: $deviceName, timeFromStart: $timeFromStart, alertState: $alertState")
        }
        call.respond(mapOf("OK" to true))
        raspberryCallManager.pingRaspberryToMakePhoto()
    }
}

fun Route.receiveHeartbeatRoute(monitoring: Monitoring, logger: Logger) {
    post("/heartbeat") {
        val heartbeatData = call.receive<HeartbeatData>().apply {
            logger.info("Heartbeat - deviceName: $deviceName, timeFromStart: $timeFromStart, timeFromAlert: $timeFromAlert")
        }
        call.respond(mapOf("OK" to true))
        monitoring.onReceivedHeartbeat(heartbeatData)
    }
}