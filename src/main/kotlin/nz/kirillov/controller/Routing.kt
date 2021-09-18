package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.routing.routing
import org.kodein.di.instance
import org.kodein.di.ktor.controller.controller

fun Application.configureRouting() {

    routing {
        controller { StudentController(instance()) }
    }
}
