package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import kotlin.math.floor
import kotlin.math.roundToInt

typealias SmartLength = Length

sealed interface Length {
    val factorCmToInches: Double
        get() = 0.3937007874
    val factorFeetToInches: Double
        get() = 12.0


    fun <T:SmartLength>convert (target: LengthUnits): T
    data class Cm(val cm: Int): SmartLength {
        override fun <T:SmartLength>convert(
            target: LengthUnits,
        ): T{
            return when (target) {
                LengthUnits.CM -> this
                LengthUnits.FEET_INCHES -> {
                    val inchesTotal = cm * factorCmToInches
                    println("----- inches total -------- $inchesTotal")
                    val feet = floor(inchesTotal / factorFeetToInches).toInt()
                    println("----- feet -------- $feet")
                    val inches = (inchesTotal - feet * factorFeetToInches).roundToInt()
                    println("----- inches -------- $inches")
                    FeetInches(feet,inches)
                }
            } as T
    }

        data class Inches(val inches: Int): SmartLength {

        override fun <T:SmartLength>convert(
            target: LengthUnits,
        ): T {
            return when(target){
                LengthUnits.CM -> {
                    val cm = (inches / factorCmToInches).roundToInt()
                    Cm(cm)
                }
                LengthUnits.FEET_INCHES -> {
                    val feet = floor(inches / factorFeetToInches).toInt()
                    val inches = (inches - feet * factorFeetToInches).roundToInt()
                    FeetInches(feet, inches)
                }
            } as T
        }
    }
    }

    data class Feet(val feet: Int): SmartLength {
        override fun <T:SmartLength>convert(
            target: LengthUnits,
        ): T {
            return this as T
        }

    }

    data class FeetInches(val feet: Int, val inches: Int, ): SmartLength {
        override fun <T:SmartLength>convert(
            target: LengthUnits,
        ): T{
            return when(target){
                LengthUnits.CM -> {
                    val totalInches = feet * factorFeetToInches + inches
                    val cm = (totalInches / factorCmToInches).roundToInt()
                    Cm(cm)
                }
                LengthUnits.FEET_INCHES ->  this
            } as T
        }

    }
}