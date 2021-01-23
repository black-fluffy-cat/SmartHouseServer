package com.jj.smarthouseserver

import com.jj.smarthouseserver.koin.smartHouseModule
import com.jj.smarthouseserver.monitoring.Monitoring
import com.jj.smarthouseserver.routes.registerRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

private const val SERVER_PORT = 8080

fun main() {

    embeddedServer(Netty, SERVER_PORT) {
        install(ContentNegotiation) { register(ContentType.Application.Json, JacksonConverter()) }
        install(Koin) { modules(smartHouseModule) }
        registerRoutes()

        Monitoring.startMonitoring()
    }.start(wait = true)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
}

