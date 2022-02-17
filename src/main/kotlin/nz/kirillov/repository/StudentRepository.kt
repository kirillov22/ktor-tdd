package nz.kirillov.repository

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nz.kirillov.model.AddStudentRequest
import nz.kirillov.model.Student
import nz.kirillov.model.StudentDoesNotExistException
import java.io.FileWriter

class StudentRepository {

    fun getStudents(): List<Student> {
        return readDataFromFile()
    }

    fun getStudentById(id: Int): Student? {
        val students = readDataFromFile()
        return students.find { it.id == id }
    }

    fun addStudent(newStudentReq: AddStudentRequest): Int {
        val existingStudents = readDataFromFile()
        val newId = existingStudents.last().id + 1

        val newStudent = Student(newId, newStudentReq.name, newStudentReq.dateOfBirth, newStudentReq.enrolledClasses, newStudentReq.averageGpa)
        existingStudents.add(newStudent)
        writeDataToFile(existingStudents)

        return newId
    }

    fun updateStudent(updatedStudent: Student): Student {
        val existingStudents = readDataFromFile()
        val existingStudent = existingStudents.find { it.id == updatedStudent.id } ?: throw StudentDoesNotExistException()

        existingStudents.remove(existingStudent)
        existingStudents.add(updatedStudent)
        writeDataToFile(existingStudents)

        return updatedStudent
    }

    fun deleteStudent(id: Int) {
        val existingStudents = readDataFromFile()
        val studentToDelete = existingStudents.find { it.id == id } ?: throw StudentDoesNotExistException()

        existingStudents.remove(studentToDelete)
        writeDataToFile(existingStudents)
    }

    private fun readDataFromFile(): MutableList<Student> {
        val rawData = this::class.java.classLoader.getResource("students.json")!!.readText()
        val students = Json.decodeFromString<List<Student>>(rawData)

        // ensure we maintain sorting by id for inserts
        return students.sortedBy { it.id }.toMutableList()
    }

    private fun writeDataToFile(students: List<Student>) {
        val fileWriter = FileWriter(this::class.java.classLoader.getResource("students.json")!!.file)
        val data = Json.encodeToString(students)
        fileWriter.write(data)
        fileWriter.close()
    }
}
