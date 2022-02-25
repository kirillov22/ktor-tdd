package nz.kirillov.repository

import nz.kirillov.model.AddStudentRequest
import nz.kirillov.model.Student

class StudentRepository {

    fun getStudents(): List<Student> {
        TODO()
    }

    fun getStudentById(id: Int): Student? {
        TODO()
    }

    fun addStudent(newStudentReq: AddStudentRequest): Int {
        TODO()
    }

    fun updateStudent(updatedStudent: Student): Student {
        TODO()
    }

    fun deleteStudent(id: Int) {
        TODO()
    }
}
