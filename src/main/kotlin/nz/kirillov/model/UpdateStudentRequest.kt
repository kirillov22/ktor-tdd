package nz.kirillov.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
class UpdateStudentRequest(
        val name: String,
        @Serializable(with = LocalDateIso8601Serializer::class)
        val dateOfBirth: LocalDate,
        val enrolledClasses: List<Subject>) {}
