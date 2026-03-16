package nl.codingwithlinda.unit_conversion.data.distance


/**
 * Represents a metric for distance measurement used in unit conversion.
 *
 * @property factorToBase The conversion factor used to transform this unit into the
 * base unit (Meters).
 *//*

 */
sealed interface Distance {
    val factorToBase: Double
}
object KM: Distance {
    override val factorToBase: Double = 1000.0
}
object METER: Distance {
    override val factorToBase: Double = 1.0
}

object CM: Distance {
    override val factorToBase: Double = 0.01
}

object MILE: Distance {
    override val factorToBase: Double = 1609.34
}

/**
 * Represents a concrete distance measurement consisting of a numerical value and its associated [Distance] unit.
 *
 * This interface is implemented by specific unit data classes to provide a type-safe container
 * for distances, allowing for easy conversion between different units of measurement.
 *
 * @property value The numerical magnitude of the distance measurement.
 * @property distance The specific unit type (e.g., [KM], [METER]) associated with this measurement.
 *//*

 */
sealed interface ConcreteDistance {
    val value: Double
    val distance: Distance


    data class km(override val value: Double) : ConcreteDistance {
        override val distance: Distance
            get() = KM
    }

    data class meter(override val value: Double) : ConcreteDistance {
        override val distance: Distance = METER
    }

    data class cm(override val value: Double) : ConcreteDistance {
        override val distance: Distance = CM
    }

    data class mile(override val value: Double) : ConcreteDistance {
        override val distance: Distance = MILE
    }
}
