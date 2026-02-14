package nl.codingwithlinda.smartstep.core.domain.model.settings

data class UserSettings(
    val gender: Gender = Gender.FEMALE,
    val weightGrams: Double = 60_000.0,
    val heightCm: Int = 170,
)