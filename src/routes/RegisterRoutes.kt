package com.jj.smarthouseserver.routes

import io.ktor.application.*

fun Application.registerRoutes() {
    monitoringRoutes()
    nodesDataRoutes()
    pingRoutes()
    raspberryRoutes()
}