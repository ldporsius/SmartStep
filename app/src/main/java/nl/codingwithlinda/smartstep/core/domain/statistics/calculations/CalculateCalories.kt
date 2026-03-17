package nl.codingwithlinda.smartstep.core.domain.statistics.calculations

import nl.codingwithlinda.smartstep.core.domain.model.settings.Gender

const val maleCaloryFactor = 1.0
const val femaleCaloryFactor = 0.9

fun kcalPerStep (weightKg: Double, gender: Gender): Double {
    return when (gender) {
        Gender.MALE -> weightKg * maleCaloryFactor * 0.0005
        Gender.FEMALE -> weightKg * femaleCaloryFactor * 0.0005
    }
}

fun caloriesBurned(steps: Int, weightKg: Double, gender: Gender): Double {
    return steps * kcalPerStep(weightKg, gender)
}
