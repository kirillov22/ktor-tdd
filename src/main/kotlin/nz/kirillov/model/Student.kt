package nz.kirillov.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateComponentSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: Int,
    val name: String,
    @Serializable(with = LocalDateComponentSerializer::class)
    val dateOfBirth: LocalDate,
    val enrolledClasses: List<Subject>,
    val averageGpa: Double
)
