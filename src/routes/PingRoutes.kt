package com.jj.smarthouseserver.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Application.pingRoutes() {
    routing { serverHealthCheckRoute() }
}

fun Route.serverHealthCheckRoute() {
    get("/health") {
        withContext(Dispatchers.IO) { call.respond("Running") }
    }
}