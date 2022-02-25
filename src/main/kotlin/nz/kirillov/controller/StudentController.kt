package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import nz.kirillov.service.StudentService
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class StudentController(application: Application) : AbstractDIController(application) {
    private val studentService: StudentService by instance()

    override fun Route.getRoutes() {
        route("/students") {
            get {
                TODO()
            }

            get("{id}") {
                TODO()
            }

            post {
                TODO()
            }

            put("{id}") {
                TODO()
            }

            delete("{id}") {
                TODO()
            }
        }
    }
}
