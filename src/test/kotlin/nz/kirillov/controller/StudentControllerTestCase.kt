package nz.kirillov.controller

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.json
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nz.kirillov.model.Student
import nz.kirillov.model.Subject
import nz.kirillov.model.SubjectName
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
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Get, "/students/abc").apply {
                every { studentService.getStudentById(any()) } returns null
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

    private fun getTestStudents(): List<Student> {
        val student1 = getTestStudent()
        val birthDate2 = LocalDate(1992, 7, 18)

        val subjects = listOf(Subject(SubjectName.COMPUTER_SCIENCE, 5.5f), Subject(SubjectName.MUSIC, 4.8f))
        val student2 = Student(321, "Alice", birthDate2, subjects, 5.15)
        return listOf(student1, student2)
    }

    private fun getTestStudent(): Student {
        val birthDate1 = LocalDate(1990, 3, 4)
        return Student(123, "Bill", birthDate1, emptyList(), 0.0)
    }
}