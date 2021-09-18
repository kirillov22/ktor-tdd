package nz.kirillov.model

import java.time.LocalDate

data class Student(
    val id: Int,
    val name: String,
    val dateOfBirth: LocalDate,
    val enrolledClasses: List<Subject>,
    val averageGpa: Double
)
