package nz.kirillov

import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import nz.kirillov.controller.configureRouting
import nz.kirillov.repository.StudentRepository
import nz.kirillov.service.StudentService
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        di {
            bind { singleton { StudentRepository() }}
            bind { singleton { StudentService(instance()) } }
        }
        configureRouting()
    }.start(wait = true)
}