package com.jj.smarthouseserver.routes

import com.jj.smarthouseserver.houseSystemState.HouseSystemStateManager
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Application.houseSystemStateRoutes() {
    val houseSystemStateManager by inject(HouseSystemStateManager::class.java)
    routing { getHouseSystemState(houseSystemStateManager) }
}

fun Route.getHouseSystemState(houseSystemStateManager: HouseSystemStateManager) {
    get("/houseState") {
        val systemState = houseSystemStateManager.getHouseSystemState()
        call.respond(systemState)
    }
}