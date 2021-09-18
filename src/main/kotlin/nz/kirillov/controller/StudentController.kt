package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import io.ktor.routing.route
import nz.kirillov.service.StudentService
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class StudentController(application: Application) : AbstractDIController(application) {
    private val studentService: StudentService by instance()

    override fun Route.getRoutes() {
        route("/student") {
            get {
                call.respondText { studentService.getStudents().toString() }
            }

            get("{id}") {
                call.respondText { "Single student" }
            }

            post {
                call.respondText("Created student")
            }

            patch("{id}") {
                call.respondText("Updated student")
            }

            delete {
                call.respondText("Deleted student")
            }
        }
    }
}