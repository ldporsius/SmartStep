package nl.codingwithlinda.unit_conversion.data.distance

import nl.codingwithlinda.unit_conversion.data.distance.ConcreteDistance.*


/**
 * Utility object responsible for converting measurements between different distance units.
 *//*

 */
object DistanceConverter {

    fun toKm(from: ConcreteDistance): km {
        return convertDistance(from, KM)
    }
    fun toMeter(from: ConcreteDistance): meter {
        return convertDistance(from, METER)
    }
    fun toCm(from: ConcreteDistance): cm {
        return convertDistance(from, CM)
    }
    fun toMile(from: ConcreteDistance): mile {
        return convertDistance(from, MILE)
    }

    private inline fun <reified T: ConcreteDistance>convertDistance(from: ConcreteDistance, to: Distance): T{
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
        }as T
    }

    private fun convertDistance(value: Double, from: Distance, to: Distance): Double{
        return value * from.factorToBase / to.factorToBase
    }
}