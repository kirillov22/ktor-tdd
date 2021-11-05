package nz.kirillov.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class AddStudentRequest(
    val name: String,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val dateOfBirth: LocalDate,
    val enrolledClasses: List<Subject>,
    val averageGpa: Double
)

@Serializable
data class AddStudentResponse(val id: Int)
