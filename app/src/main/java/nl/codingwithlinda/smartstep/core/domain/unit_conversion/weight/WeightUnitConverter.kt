package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits

object WeightUnitConverter {

    val kgToPounds = 2.20462

    fun convert(value: Double, from: UnitSystemUnits, to: WeightUnits): Double {
        when (from) {
            UnitSystemUnits.IMPERIAL -> {
                return when(to.system){
                    UnitSystemUnits.IMPERIAL -> {
                        value
                    }
                    UnitSystemUnits.SI -> {
                        toSI(value)
                    }
                }
            }
             UnitSystemUnits.SI -> {
                return when(to.system){
                        UnitSystemUnits.IMPERIAL -> {
                            toImperial(value)
                        }
                        UnitSystemUnits.SI -> {
                            value
                        }
                    }
                }
            }
        }


    private fun toSI(pounds: Double): Double{
        return (pounds / kgToPounds)
    }

    private fun toImperial(kg: Double): Double{
        return (kg * kgToPounds)
    }

}