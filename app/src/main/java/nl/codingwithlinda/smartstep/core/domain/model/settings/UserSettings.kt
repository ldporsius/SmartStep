package nl.codingwithlinda.smartstep.core.domain.model.settings

data class UserSettings(
    val gender: Gender = Gender.FEMALE,
    val weight: Double = 60.0,
    val height: Int = 170,
)