package nl.codingwithlinda.unit_conversion.data.lenght

import kotlin.math.floor
import kotlin.math.roundToInt


interface Length {
    val valueCm: Double
    val baseFactor: Double
}

interface CmToFeetInchesAdapter: Length {
    fun convert(): FeetInches
    val factorCmToInches: Double
        get() = 0.3937007874
    val factorFeetToInches: Double
        get() = 12.0
}


data class Cm(
    override val valueCm: Double
): CmToFeetInchesAdapter{
    override val baseFactor: Double
        get() = 1.0

    override fun convert(): FeetInches {
        val inchesTotal = valueCm * factorCmToInches

        val feet = (inchesTotal / factorFeetToInches)

        val wholeFeet = floor(feet).toInt()

        val remainder = feet - wholeFeet

        val inches = (remainder * factorFeetToInches).roundToInt()

        //used to round 12 inches up to 1 feet
        val modInches = inches.mod(factorFeetToInches.toInt()).let {
            if(it == 0) 1 else 0
        }

        val feetInInches = wholeFeet + modInches

        val remainingInches = inches - modInches * inches

        return FeetInches(feetInInches,remainingInches)
    }
}

data class FeetInches(
    val feet: Int,
    val inches: Int,
): Length {

    private val feetToInchesFactor: Double = 12.0
    override val baseFactor: Double
        get() = 2.54

    override val valueCm: Double
        get() {
            val totalInches = feet * feetToInchesFactor + inches
            return totalInches * baseFactor
        }
}

