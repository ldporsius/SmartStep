package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import nl.codingwithlinda.smartstep.core.data.unit_conversion.CmToInchesConverter
import nl.codingwithlinda.smartstep.core.data.unit_conversion.InchesToFeetConverter
import kotlin.math.floor
import kotlin.math.roundToInt

object HeightUnitConverter {

    val cmToInches = CmToInchesConverter()
    val inchesToFeet = InchesToFeetConverter()
    private fun convert(from: Double): Double {
        val inches = cmToInches.convert(from)
        val feet = inchesToFeet.convert(inches)
        return feet
    }


    fun toImperial(from: Double): Pair<String, String>{
        val feet_inches = convert(from)

        val feet = floor(feet_inches).toInt()

        val inches = cmToInches.convert(from) - inchesToFeet.factorToFeet * feet
        val roundedInches = (inches).roundToInt()
        return Pair(feet.toString(), roundedInches.toString())
    }

    fun toSI(feet: Int, inches: Int): Double {
        val totalInches = feet.toDouble() * inchesToFeet.factorToFeet + inches.toDouble()
        return totalInches / cmToInches.factorToInches  // ✅ Divide to go back to cm
    }
}