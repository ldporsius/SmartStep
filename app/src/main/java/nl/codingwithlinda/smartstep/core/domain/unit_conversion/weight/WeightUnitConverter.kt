package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems

object WeightUnitConverter {

    val kgToPounds = 2.20462

    fun convert(value: Double, from: UnitSystems, to: UnitSystems): Double {
        when (from) {
            UnitSystems.IMPERIAL -> {
                return when(to){
                    UnitSystems.IMPERIAL -> {
                        value
                    }
                    UnitSystems.SI -> {
                        toSI(value).toDouble()
                    }
                }
            }
             UnitSystems.SI -> {
                return when(to){
                        UnitSystems.IMPERIAL -> {
                            toImperial(value)
                        }
                        UnitSystems.SI -> {
                            value
                        }
                    }
                }
            }
        }

    private fun Double.toGrams(): Int{
        return (this * 1000).toInt()
    }
    private fun Double.toKg(): Double{
        return this / 1000
    }

    private fun toSI(pounds: Double): Int{
        return (pounds / kgToPounds).toGrams()
    }

    private fun toImperial(grams: Double): Double{
        return grams.toKg() * kgToPounds
    }

}