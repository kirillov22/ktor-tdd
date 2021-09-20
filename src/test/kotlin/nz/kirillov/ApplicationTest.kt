package nz.kirillov

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import nz.kirillov.controller.configureRouting
import nz.kirillov.repository.StudentRepository
import nz.kirillov.service.StudentService
import org.assertj.core.api.Assertions.assertThat
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({
                di {
                    bind { singleton { StudentRepository() }}
                    bind { singleton { StudentService(instance()) } }
                }
                configureRouting()
            }) {
            handleRequest(HttpMethod.Get, "/students").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content).isEqualTo("foo")
            }
        }
    }
}