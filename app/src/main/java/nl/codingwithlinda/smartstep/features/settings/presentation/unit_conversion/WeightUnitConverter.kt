package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import kotlin.math.roundToInt

class WeightUnitConverter {

    val kgToPounds = 2.20462

    fun toSI(pounds: Int): Int{
        return (pounds / kgToPounds).roundToInt()
    }

    fun toImperial(kg: Double): Int {
        return (kg * kgToPounds).roundToInt()
    }

}