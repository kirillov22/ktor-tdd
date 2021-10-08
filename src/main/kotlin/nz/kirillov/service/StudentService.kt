package nz.kirillov.service

import nz.kirillov.model.Student
import nz.kirillov.repository.StudentRepository

class StudentService(private val repository: StudentRepository) {

    fun getStudents(): List<Student> {
        TODO()
    }

    fun getStudentById(id: Int): Student? {
        TODO()
    }

    fun updateStudent(updatedStudent: Student) {
        TODO()
    }

    fun deleteStudent(id: Int) {
        TODO()
    }
}