package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import nz.kirillov.model.AddStudentRequest
import nz.kirillov.model.AddStudentResponse
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
                val student: AddStudentRequest
                try {
                    student = call.receive()
                } catch (e: Exception) {
                    println(e)
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Bad Request")
                    return@post
                }

                val newId = studentService.addStudent(student)
                call.respond(HttpStatusCode.Created, AddStudentResponse(newId))
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
