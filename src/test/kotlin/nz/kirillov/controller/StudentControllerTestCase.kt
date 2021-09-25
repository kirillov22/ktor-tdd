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
import org.assertj.core.api.Assertions
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
    }

    @Test
    fun `should return all students when GET request is made to students endpoint`() {
        withTestApplication(configure()) {
            handleRequest(HttpMethod.Get, "/students").apply {
                Assertions.assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                val expectedStudents = getTestStudents()
                val rawResponse = response.content ?: ""
                val actualStudents = Json.decodeFromString<List<Student>>(rawResponse)
                Assertions.assertThat(actualStudents).isEqualTo(expectedStudents)
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
        val birthDate1 = LocalDate(1990, 3, 4)
        val birthDate2 = LocalDate(1992, 7, 18)
        val student1 = Student(1, "Bill", birthDate1, emptyList(), 0.0)
        val subjects = listOf(Subject(SubjectName.COMPUTER_SCIENCE, 5.5f), Subject(SubjectName.MUSIC, 4.8f))
        val student2 = Student(1, "Alice", birthDate2, subjects, 5.15)
        return listOf(student1, student2)
    }
}