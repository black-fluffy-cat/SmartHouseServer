package com.jj.smarthouseserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Application.downloadDataRoutes() {
    routing { shareBigFile() }
}

fun Route.shareBigFile() {
    get("/bigFileDownload") {
        val filepath = "/home/j/Pulpit/smarthouse_projects/KtorSmartHouseServer/dummyDownloadFile.xyz"
        val file = File(filepath)
        if (file.exists()) {
            call.respondFile(file)
        } else call.respond(HttpStatusCode.NotFound)
    }
}