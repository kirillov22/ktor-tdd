package nz.kirillov.controller

import kotlinx.datetime.LocalDate
import nz.kirillov.model.Student
import nz.kirillov.model.Subject
import nz.kirillov.model.SubjectName

fun getTestStudents(): List<Student> {
    val student1 = getTestStudent()
    val birthDate2 = LocalDate(1992, 7, 18)

    val subjects = listOf(Subject(SubjectName.COMPUTER_SCIENCE, 5.5f), Subject(SubjectName.MUSIC, 4.8f))
    val student2 = Student(321, "Alice", birthDate2, subjects, 5.15)
    return listOf(student1, student2)
}

fun getTestStudent(): Student {
    val birthDate1 = LocalDate(1990, 3, 4)
    return Student(123, "Bill", birthDate1, emptyList(), 0.0)
}
