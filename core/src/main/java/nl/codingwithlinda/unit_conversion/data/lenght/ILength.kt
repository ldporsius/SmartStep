package nl.codingwithlinda.unit_conversion.data.lenght

import nl.codingwithlinda.unit_conversion.domain.LengthUnitType

sealed interface Length: LengthUnitType{
    object CM: Length {
        override val baseFactor: Double
            get() = 1.0
    }

    object INCH: Length {
        override val baseFactor: Double
            get() = 2.54
    }
    object FEET: Length {
        override val baseFactor: Double
            get() = 30.48
    }
}
