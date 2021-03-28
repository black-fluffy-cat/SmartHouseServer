package com.jj.smarthouseserver

import com.jj.smarthouseserver.koin.smartHouseModule
import com.jj.smarthouseserver.monitoring.Monitoring
import com.jj.smarthouseserver.routes.registerRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

private const val SERVER_PORT = 8080

fun main() {}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    embeddedServer(Netty, SERVER_PORT) {
        install(ContentNegotiation) { json() }
        install(Koin) { modules(smartHouseModule) }
        registerRoutes()

        Monitoring.startMonitoring()
    }.start(wait = true)
}

