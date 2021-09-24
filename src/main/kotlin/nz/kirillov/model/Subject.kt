package nz.kirillov.model

import kotlinx.serialization.Serializable

@Serializable
data class Subject(val name: SubjectName, val grade: Float)

enum class SubjectName {
    ACCOUNTING,
    ASTRONOMY,
    BUSINESS,
    BIOLOGY,
    CHEMISTRY,
    COMPUTER_SCIENCE,
    GEOGRAPHY,
    GEOLOGY,
    ECONOMICS,
    MATHEMATICS,
    MUSIC,
    PHYSICS
}