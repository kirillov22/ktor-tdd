package nz.kirillov.model

class StudentDoesNotExistException(message: String = "Given student does not exist") : Exception(message) {}
