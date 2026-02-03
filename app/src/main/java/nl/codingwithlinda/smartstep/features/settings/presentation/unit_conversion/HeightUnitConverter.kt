package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.smartstep.core.data.unit_conversion.CmToInchesConverter
import nl.codingwithlinda.smartstep.core.data.unit_conversion.InchesToFeetConverter
import kotlin.math.floor
import kotlin.math.roundToInt

class HeightUnitConverter {

    val cmToInches = CmToInchesConverter()
    val inchesToFeet = InchesToFeetConverter()
    private fun convert(from: Double): Double {
        val inches = cmToInches.convert(from)
        val feet = inchesToFeet.convert(inches)
        return feet
    }


    fun toUi(from: Double): Pair<String, String>{
        val feet_inches = convert(from)
        //println("feet_inches = $feet_inches")
        val feet = floor( feet_inches).toInt()
        //println("feet = $feet")

        val inches = cmToInches.convert(from) - inchesToFeet.factorToFeet * feet
        val roundedInches = (inches).roundToInt()
        //println("inches = $roundedInches")
        return Pair(feet.toString(), roundedInches.toString())
    }

   /* fun fromUi(feet: String, inches: String): Double {
        val totalInches = feet.toDouble() * inchesToFeet.factorToFeet + inches.toDouble()
        return cmToInches.convert(totalInches)

    }*/

    fun fromUi(feet: String, inches: String): Double {
        val totalInches = feet.toDouble() * inchesToFeet.factorToFeet + inches.toDouble()
        return totalInches / cmToInches.factorToInches  // ✅ Divide to go back to cm
    }
}