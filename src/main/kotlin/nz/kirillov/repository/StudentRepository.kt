package nz.kirillov.repository

import kotlinx.datetime.LocalDate
import nz.kirillov.model.Student

class StudentRepository {

    fun getStudents(): List<Student> {
        return listOf(Student(1, "bill", LocalDate(1990, 3, 4), emptyList(), 0.1))
    }

    fun getStudentById(id: Int) {
        TODO()
    }

    fun updateStudent(updatedStudent: Student) {
        TODO()
    }

    fun deleteStudent(id: Int) {
        TODO()
    }
}