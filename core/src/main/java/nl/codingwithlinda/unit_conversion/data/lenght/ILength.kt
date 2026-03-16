package nl.codingwithlinda.unit_conversion.data.lenght

sealed interface Length{
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

    val baseFactor: Double

}

interface LengthValue{
    val value: Double
    val type: Length
}
