package nl.codingwithlinda.smartstep.core.domain.model.settings

data class UserSettings(
    val gender: Gender = Gender.FEMALE,
    val weight: Int = 60,
    val height: Int = 170,
)