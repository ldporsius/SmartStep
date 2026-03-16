package nl.codingwithlinda.unit_conversion.data.lenght

import nl.codingwithlinda.unit_conversion.domain.LengthUnitType
import nl.codingwithlinda.unit_conversion.domain.UnitValue

class LengthUnitConverter {

    data class Cm(
        override val value: Double,
    ) : UnitValue {
        override val type: LengthUnitType = Length.CM
    }

    data class Inch(
        override val value: Double
    ) : UnitValue {
        override val type: LengthUnitType
            get() = Length.INCH
    }

    data class Feet(override val value: Double) : UnitValue {
        override val type: LengthUnitType
            get() = Length.FEET
    }

    fun toCm(from: UnitValue): Cm{
        return convert(from, Length.CM)
    }
    fun toInch(from: UnitValue): Inch{
        return convert(from, Length.INCH)
    }
    fun toFeet(from: UnitValue): Feet{
        return convert(from, Length.FEET)
    }

   private inline fun <reified U: UnitValue> convert(from: UnitValue, to: Length): U {
        return when (to) {
            Length.CM -> Cm((from.value / to.baseFactor) * from.type.baseFactor)
            Length.INCH -> Inch((from.value / to.baseFactor) * from.type.baseFactor)
            Length.FEET -> Feet((from.value / to.baseFactor) * from.type.baseFactor)
        } as U
    }

}

