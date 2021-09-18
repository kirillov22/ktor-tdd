package nz.kirillov.repository

import nz.kirillov.model.Student
import java.time.LocalDate

class StudentRepository {

    fun getStudents(): List<Student> {
        return listOf(Student(1,"bill", LocalDate.now(), emptyList(), 0.1))
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