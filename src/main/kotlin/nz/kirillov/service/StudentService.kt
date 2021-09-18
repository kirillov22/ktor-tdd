package nz.kirillov.service

import nz.kirillov.model.Student
import nz.kirillov.repository.StudentRepository

class StudentService(private val repository: StudentRepository) {

    fun getStudents(): List<Student> {
        return repository.getStudents()
    }

}