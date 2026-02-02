package nl.codingwithlinda.smartstep.core.data.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitConverter
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitType

class CmToInchesConverter: UnitConverter<Double>, UnitType.LENGTH {
    val factorToInches = 0.3937007874
    override fun convert(from: Double): Double {
        return from * factorToInches
    }
}

class InchesToFeetConverter: UnitConverter<Double>, UnitType.LENGTH {
    val factorToFeet = 12
    override fun convert(from: Double): Double {
        return from / factorToFeet
    }

}