package nl.codingwithlinda.unit_conversion.data.lenght

object LengthUnitConverter {

    data class Cm(
        override val value: Double,
    ) : LengthValue{
        override val type: Length = Length.CM
    }

    data class Inch(
        override val value: Double
    ) : LengthValue {
        override val type
            get() = Length.INCH
    }

    data class Feet(override val value: Double) : LengthValue {
        override val type
            get() = Length.FEET
    }

    fun toCm(from: LengthValue): Cm{
        return convert(from, Length.CM)
    }
    fun toInch(from: LengthValue): Inch{
        return convert(from, Length.INCH)
    }
    fun toFeet(from: LengthValue): Feet{
        return convert(from, Length.FEET)
    }

   private inline fun <reified U: LengthValue> convert(from: LengthValue, to: Length): U {
        return when (to) {
            Length.CM -> Cm((from.value / to.baseFactor) * from.type.baseFactor)
            Length.INCH -> Inch((from.value / to.baseFactor) * from.type.baseFactor)
            Length.FEET -> Feet((from.value / to.baseFactor) * from.type.baseFactor)
        } as U
    }

}

