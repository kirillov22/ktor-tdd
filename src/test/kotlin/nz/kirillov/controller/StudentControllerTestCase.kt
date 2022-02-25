package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.json
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nz.kirillov.model.AddStudentResponse
import nz.kirillov.model.Student
import nz.kirillov.repository.StudentRepository
import nz.kirillov.service.StudentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton

class StudentControllerTestCase {

    @MockK
    private lateinit var studentRepository: StudentRepository

    @MockK
    private lateinit var studentService: StudentService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { studentService.getStudents() } returns getTestStudents()
        every { studentService.getStudentById(any()) } returns getTestStudent()
    }

    @Test
    fun `should return all students when GET request is made to students endpoint`() {
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Get, "/students").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                val expectedStudents = getTestStudents()
                val rawResponse = response.content ?: ""
                val actualStudents = Json.decodeFromString<List<Student>>(rawResponse)
                assertThat(actualStudents).isEqualTo(expectedStudents)
            }
        }
    }

    @Test
    fun `should return a single student when GET request is made to students endpoint with a valid id`() {
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Get, "/students/123").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                val expectedStudent = getTestStudent()
                val rawResponse = response.content ?: ""
                val actualStudent = Json.decodeFromString<Student>(rawResponse)
                assertThat(actualStudent).isEqualTo(expectedStudent)
            }
        }
    }

    @Test
    fun `should return a 404 status code when GET request is made to students endpoint with an id that does not match any students`() {
        every { studentService.getStudentById(any()) } returns null
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Get, "/students/999").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.NotFound)
            }
        }
    }

    @Test
    fun `should return a 400 status code when GET request is made to students endpoint with an id that is not an integer`() {
        every { studentService.getStudentById(any()) } returns null
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Get, "/students/abc").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `should return a 201 status code when POST request is made to students endpoint that matches the expected api spec`() {
        every { studentService.addStudent(any()) } returns 123
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Post, "/students") {
                addHeader("content-type", "application/json")
                setBody(getValidAddStudent())
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Created)
            }
        }
    }

    @Test
    fun `should return the new student id when POST request is made to students endpoint that matches the expected api spec`() {
        every { studentService.addStudent(any()) } returns 123
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Post, "/students") {
                addHeader("content-type", "application/json")
                setBody(getValidAddStudent())
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Created)
                val rawResponse = response.content ?: ""
                val addStudentResponse = Json.decodeFromString<AddStudentResponse>(rawResponse)
                assertThat(addStudentResponse.id).isEqualTo(123)
            }
        }
    }

    @Test
    fun `should return a 400 status code when POST request is made to students endpoint that does not the expected api spec`() {
        every { studentService.addStudent(any()) } returns 123
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Post, "/students") {
                addHeader("content-type", "application/json")
                setBody(getInvalidAddStudent())
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    private fun configure(): Application.() -> Unit = {
        install(ContentNegotiation) {
            json()
        }
        di {
            bind { singleton { studentRepository }}
            bind { singleton { studentService } }
        }
        configureRouting()
    }

    private fun getValidAddStudent(): String {
        return "{\n" +
                "    \"name\": \"Tom Banks\",\n" +
                "    \"dateOfBirth\": \"2021-11-04\",\n" +
                "    \"enrolledClasses\": [],\n" +
                "    \"averageGpa\": \"0.0\"\n" +
                "}"
    }

    private fun getInvalidAddStudent(): String {
        return "{\n" +
                "    \"name\": \"Tom Banks\",\n" +
                "    \"dateOfBirth\": \"2021-aa-04\",\n" +
                "    \"enrolledClasses\": [],\n" +
                "    \"averageGpa\": \"ccc\"\n" +
                "}"
    }
}
