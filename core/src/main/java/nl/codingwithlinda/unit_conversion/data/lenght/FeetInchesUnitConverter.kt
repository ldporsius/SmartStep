package nl.codingwithlinda.unit_conversion.data.lenght

import nl.codingwithlinda.unit_conversion.data.lenght.lenght_defs.FeetInches
import nl.codingwithlinda.unit_conversion.domain.UnitValue
import kotlin.math.floor
import kotlin.math.roundToInt


class FeetInchesUnitConverter  {

    val factorFeetToInches = 12
    val lengthConverter = LengthUnitConverter()

    fun convertToFeetInches(from: UnitValue): FeetInches {

        val feet = lengthConverter.toFeet(from).value

        val wholeFeet = floor(feet).toInt()

        val remainder = feet - wholeFeet

        val inches = (remainder * factorFeetToInches).roundToInt()

        //used to round 12 inches up to 1 foot
        val modInches = inches.mod(factorFeetToInches).let {
            if(it == 0) 1 else 0
        }

        val feetInInches = wholeFeet + modInches

        val remainingInches = inches - modInches * inches

        return FeetInches(feetInInches, remainingInches)
    }

    fun toCm(from: FeetInches): LengthUnitConverter.Cm{
        val totalInches = from.feet * factorFeetToInches + from.inches
        val unitValueInches = LengthUnitConverter.Inch(totalInches.toDouble())

        return lengthConverter.toCm(unitValueInches)
    }

    private inline fun <reified U: UnitValue>convertToUnitValue(from: FeetInches, to: Length): U{
        val totalInches = from.feet * factorFeetToInches + from.inches
        val unitValueInches = LengthUnitConverter.Inch(totalInches.toDouble())
        return when(to){
            Length.CM -> lengthConverter.toCm(unitValueInches) as LengthUnitConverter.Cm
            Length.INCH -> lengthConverter.toInch(unitValueInches)
            Length.FEET -> lengthConverter.toFeet(unitValueInches)
        } as U
    }

}