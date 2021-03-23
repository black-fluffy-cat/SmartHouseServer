package com.jj.smarthouseserver.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.pingRoutes() {
    routing { serverHealthCheckRoute() }
}

fun Route.serverHealthCheckRoute() {
    get("/health") { call.respond("Running") }
}