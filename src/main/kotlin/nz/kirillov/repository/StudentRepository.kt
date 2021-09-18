package nz.kirillov.repository

import nz.kirillov.model.Student
import java.time.LocalDate

class StudentRepository {
    fun getStudents(): List<Student> {
        return listOf(Student("bill", LocalDate.now(), emptyList(), 0.1))
    }
}