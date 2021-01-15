package com.jj.smarthouseserver.routes

import com.jj.smarthouseserver.managers.RaspberryCallManager
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent

fun Application.raspberryRoutes() {
    val raspberryCallManager by KoinJavaComponent.inject(RaspberryCallManager::class.java)
    routing { raspPhoto(raspberryCallManager) }
}

fun Route.raspPhoto(raspberryCallManager: RaspberryCallManager) {
    post("/raspPhoto") {
        withContext(Dispatchers.IO) {
            raspberryCallManager.pingRaspberryToMakePhoto()
            call.respond(mapOf("OK" to true))
        }
    }
}