package nz.kirillov.controller

import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nz.kirillov.model.AddStudentRequest
import nz.kirillov.model.Student
import nz.kirillov.model.StudentDoesNotExistException
import nz.kirillov.model.Subject
import nz.kirillov.model.SubjectName
import nz.kirillov.repository.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.FileWriter

class StudentRepositoryTestCase {

    private lateinit var repository: StudentRepository

    @Before
    fun setUp() {
        resetTestFile()
        repository = StudentRepository()
    }

    @Test
    fun `should return all students when getting all students and there are students`() {
        val expectedStudents = getTestStudents()
        writeDataToFile(expectedStudents)

        val actualStudents = repository.getStudents()
        assertThat(actualStudents).isEqualTo(expectedStudents)
    }

    @Test
    fun `should return no students when getting all students and there are no students`() {
        val actualStudents = repository.getStudents()

        assertThat(actualStudents).isEmpty()
    }

    @Test
    fun `should return correct student when getting a student by id`() {
        val expectedStudent = getTestStudent()
        writeDataToFile(listOf(expectedStudent))

        val actualStudent = repository.getStudentById(expectedStudent.id)
        assertThat(actualStudent).isNotNull
        assertThat(actualStudent).isEqualTo(expectedStudent)
    }

    @Test
    fun `should return null when getting a student by id that does not exist`() {
        writeDataToFile(listOf(getTestStudent()))

        val actualStudent = repository.getStudentById(NON_EXISTENT_STUDENT_ID)
        assertThat(actualStudent).isNull()
    }

    @Test
    fun `should return next highest id when adding a student`() {
        val students = getTestStudents()
        writeDataToFile(students)
        val lastId = students.last().id
        val expectedNewId = lastId + 1

        val studentToAdd = getAddStudentRequest()

        val actualNewId = repository.addStudent(studentToAdd)
        assertThat(actualNewId).isEqualTo(expectedNewId)
    }

    @Test
    fun `should update student when student exists`() {
        val students = getTestStudents()
        writeDataToFile(students)
        val oldStudent = students.last()

        val newName = "Phillip Morrison"
        val newDateOfBirth = LocalDate(1998, 5, 5)
        val studentToUpdateTo = Student(oldStudent.id, newName, newDateOfBirth, oldStudent.enrolledClasses, oldStudent.averageGpa)

        val updatedStudent = repository.updateStudent(studentToUpdateTo)
        assertThat(updatedStudent).isEqualTo(studentToUpdateTo)

        val updatedStudentFromRepository = repository.getStudentById(oldStudent.id)
        assertThat(updatedStudentFromRepository).isNotNull
        assertThat(updatedStudentFromRepository).isEqualTo(updatedStudent)
    }

    @Test
    fun `should throw error when trying to update student that does not exist`() {
        val newName = "Phillip Morrison"
        val newDateOfBirth = LocalDate(1998, 5, 5)
        val studentToUpdateTo = Student(NON_EXISTENT_STUDENT_ID, newName, newDateOfBirth, emptyList(), 0.0)

        assertThrows(StudentDoesNotExistException::class.java) {
            repository.updateStudent(studentToUpdateTo)
        }
    }

    @Test
    fun `should delete student when given student exists`() {
        val students = getTestStudents()
        writeDataToFile(students)
        val studentToDelete = students.last()

        repository.deleteStudent(studentToDelete.id)
        val actualStudent = repository.getStudentById(studentToDelete.id)
        assertThat(actualStudent).isNull()
    }

    @Test
    fun `should throw error when trying to delete student that does not exist`() {
        assertThrows(StudentDoesNotExistException::class.java) {
            repository.deleteStudent(NON_EXISTENT_STUDENT_ID)
        }
    }

    private fun resetTestFile() {
        val writer = getStudentFileWriter()
        writer.write("[]")
        writer.close()
    }

    private fun getStudentFileWriter(): FileWriter {
        return FileWriter(this::class.java.classLoader.getResource("students.json")!!.file)
    }

    private fun writeDataToFile(students: List<Student>) {
        val writer = getStudentFileWriter()
        val data = Json.encodeToString(students)

        writer.write(data)
        writer.close()
    }

    private fun getAddStudentRequest(): AddStudentRequest {
        return AddStudentRequest("Bilbo Baggins", LocalDate(1892, 1, 3), listOf(Subject(SubjectName.GEOGRAPHY,  9.0f)), 9.0)
    }

    companion object {
        const val NON_EXISTENT_STUDENT_ID = 999999
    }
}
