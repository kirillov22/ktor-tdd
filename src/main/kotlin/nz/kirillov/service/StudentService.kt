package nz.kirillov.service

import nz.kirillov.model.AddStudentRequest
import nz.kirillov.model.Student
import nz.kirillov.model.StudentDoesNotExistException
import nz.kirillov.model.UpdateStudentRequest
import nz.kirillov.repository.StudentRepository

class StudentService(private val repository: StudentRepository) {

    fun getStudents(): List<Student> {
        return repository.getStudents()
    }

    fun getStudentById(id: Int): Student? {
        return repository.getStudentById(id)
    }

    fun addStudent(student: AddStudentRequest): Int {
        return repository.addStudent(student)
    }

    fun updateStudent(studentId: Int, updatedStudent: UpdateStudentRequest): Student {
        repository.getStudentById(studentId) ?: throw StudentDoesNotExistException("Student with id: $studentId does not exist")

        val averageGpa = updatedStudent.enrolledClasses.map{ it.grade }.average()
        val student = Student(studentId, updatedStudent.name, updatedStudent.dateOfBirth, updatedStudent.enrolledClasses, averageGpa)
        return repository.updateStudent(student)
    }

    fun deleteStudent(id: Int) {
        repository.deleteStudent(id)
    }
}
