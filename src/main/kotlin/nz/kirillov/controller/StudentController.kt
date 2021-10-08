package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
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
                call.respond(studentService.getStudents())
            }

            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                studentService.getStudentById(id)?.let {
                    call.respond(it)
                    return@get
                }

                call.respond(HttpStatusCode.NotFound)
            }

            post {
                TODO()
            }

            put("{id}") {
                TODO()
            }

            delete {
                TODO()
            }
        }
    }
}