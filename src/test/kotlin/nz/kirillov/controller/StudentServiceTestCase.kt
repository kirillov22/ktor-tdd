package nz.kirillov.controller

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.datetime.LocalDate
import nz.kirillov.model.AddStudentRequest
import nz.kirillov.model.Student
import nz.kirillov.model.StudentDoesNotExistException
import nz.kirillov.model.Subject
import nz.kirillov.model.SubjectName
import nz.kirillov.model.UpdateStudentRequest
import nz.kirillov.repository.StudentRepository
import nz.kirillov.service.StudentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class StudentServiceTestCase {

    @MockK
    private lateinit var studentRepository: StudentRepository

    private lateinit var service: StudentService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        service = StudentService(studentRepository)
    }

    @Test
    fun `should return all students when getting all students and there are students`() {
        val expectedStudents = getTestStudents()
        every { studentRepository.getStudents() } returns getTestStudents()

        val result = service.getStudents()
        assertThat(result).isEqualTo(expectedStudents)
        verify { studentRepository.getStudents() }
    }

    @Test
    fun `should return empty collection of students when getting all students and there are no students`() {
        every { studentRepository.getStudents() } returns emptyList()

        val result = service.getStudents()

        assertThat(result).isEmpty()
        verify { studentRepository.getStudents() }
    }

    @Test
    fun `should call repository when getting a single student`() {
        val expectedStudent = getTestStudent()
        every { studentRepository.getStudentById(any()) } returns expectedStudent

        val result = service.getStudentById(123)

        assertThat(result).isEqualTo(expectedStudent)
        verify { studentRepository.getStudentById(123) }
    }

    @Test
    fun `should return null when getting a single student that does not exist`() {
        every { studentRepository.getStudentById(any()) } returns null

        val result = service.getStudentById(123)

        assertThat(result).isNull()
        verify { studentRepository.getStudentById(123) }
    }

    @Test
    fun `should return the next highest id for a student when adding a new student`() {
        val testStudent = getTestStudent()
        val addStudentRequest = AddStudentRequest(testStudent.name, testStudent.dateOfBirth, testStudent.enrolledClasses, testStudent.averageGpa)
        val nextStudentId = 322
        val expectedStudent = Student(nextStudentId, testStudent.name, testStudent.dateOfBirth, testStudent.enrolledClasses, testStudent.averageGpa)
        every { studentRepository.addStudent(any()) }

        val result = service.addStudent(addStudentRequest)

        assertThat(result).isEqualTo(nextStudentId)
        verify { studentRepository.addStudent(expectedStudent) }
    }

    @Test
    fun `should call repository when updating a student`() {
        val studentId = 123
        val updateStudentRequest = getUpdatedStudent(studentId).first
        val updatedStudent = getUpdatedStudent(studentId).second
        every { studentRepository.updateStudent(any()) }

        service.updateStudent(updateStudentRequest)

        verify { studentRepository.updateStudent(updatedStudent) }
    }

    @Test
    fun `should update student fields correctly when updating a student`() {
        val studentId = 123
        val updateStudentRequest = getUpdatedStudent(studentId).first
        every { studentRepository.updateStudent(any()) }

        val result = service.updateStudent(updateStudentRequest)

        assertThat(result.name).isEqualTo("Timothy")
        assertThat(result.dateOfBirth).isEqualTo(LocalDate(2001, 12, 12))
        assertThat(result.enrolledClasses).isEqualTo(UPDATED_SUBJECTS)
        assertThat(result.averageGpa).isEqualTo(3.0f)
    }

    @Test
    fun `should re-calculate student GPA when student is updated`() {
        val studentId = 123
        val updateStudentRequest = getUpdatedStudent(studentId).first
        every { studentRepository.updateStudent(any()) }

        val result = service.updateStudent(updateStudentRequest)

        assertThat(result.averageGpa).isEqualTo(3.0f)
    }

    @Test
    fun `should throw error when trying to update a student that does not exist`() {
        val studentId = 123
        val updateStudentRequest = getUpdatedStudent(studentId).first
        every { studentRepository.updateStudent(any()) } throws StudentDoesNotExistException()

        assertThrows(StudentDoesNotExistException::class.java) {
            service.updateStudent(updateStudentRequest)
        }
    }

    @Test
    fun `should call repository when deleting a student`() {
        val studentId = 123
        every { studentRepository.deleteStudent(any()) }

        service.deleteStudent(studentId)

        verify { studentRepository.deleteStudent(studentId) }
    }

    @Test
    fun `should throw error if student does not exist when trying to delete student`() {
        val studentId = 123
        every { studentRepository.deleteStudent(any()) } throws StudentDoesNotExistException()

        assertThrows(StudentDoesNotExistException::class.java) {
            service.deleteStudent(studentId)
        }
    }

    private fun getTestStudents(): List<Student> {
        val student1 = getTestStudent()
        val birthDate2 = LocalDate(1992, 7, 18)

        val subjects = listOf(Subject(SubjectName.COMPUTER_SCIENCE, 5.5f), Subject(SubjectName.MUSIC, 4.8f))
        val student2 = Student(321, "Alice", birthDate2, subjects, 5.15)
        return listOf(student1, student2)
    }

    private fun getUpdatedStudent(id: Int): Pair<UpdateStudentRequest, Student> {
        val name = "Timothy"
        val updatedBirthDate = LocalDate(2001, 12, 12)
        val subjects = UPDATED_SUBJECTS
        val averageGpa = subjects.map{it.grade}.average()

        val student = Student(id, name, updatedBirthDate, subjects, averageGpa)
        return Pair(UpdateStudentRequest(name, updatedBirthDate, subjects), student)
    }

    private fun getTestStudent(): Student {
        val birthDate1 = LocalDate(1990, 3, 4)
        return Student(123, "Bill", birthDate1, emptyList(), 0.0)
    }

    companion object {
        val UPDATED_SUBJECTS = listOf(
            Subject(SubjectName.BIOLOGY, 2.0f),
            Subject(SubjectName.ASTRONOMY, 4.0f),
            Subject(SubjectName.PHYSICS, 3.0f)
        )
    }
}
