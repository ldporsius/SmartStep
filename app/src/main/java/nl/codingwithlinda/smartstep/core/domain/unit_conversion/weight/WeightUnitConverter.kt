package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import kotlin.math.roundToInt

object WeightUnitConverter {

    val kgToPounds = 2.20462

    fun toSI(pounds: Int): Int{
        return (pounds / kgToPounds).roundToInt()
    }

    fun toImperial(kg: Double): Int {
        return (kg * kgToPounds).roundToInt()
    }

}