package nl.codingwithlinda.unit_conversion.data

import nl.codingwithlinda.unit_conversion.data.convertDistance

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

private fun convertDistance(value: Double, from: Distance, to: Distance): Double{
    return value * from.factorToBase / to.factorToBase
}

fun convertDistance(from: ConcreteDistance, to: Distance): ConcreteDistance{
    val converted = convertDistance(from.value, from.distance, to)
    return when(to){
        KM -> {
            km(converted)
        }
        METER -> {
            meter(converted)
        }
        CM -> {
            cm(converted)
        }
        MILE -> {
            mile(converted)
        }
    }
}

interface ConcreteDistance{
    val value: Double
    val distance: Distance
}

data class km(override val value: Double): ConcreteDistance {
    override val distance: Distance
        get() = KM

}
data class meter(override val value: Double): ConcreteDistance {
    override val distance: Distance = METER
}
data class cm(override val value: Double): ConcreteDistance {
    override val distance: Distance = CM
}
data class mile(override val value: Double): ConcreteDistance {
    override val distance: Distance = MILE

}
