package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

class CmToInchesConverter {
    val factorToInches = 0.3937007874
    fun convert(from: Double): Double {
        return from * factorToInches
    }
}

class InchesToFeetConverter{
    val factorToFeet = 12
    fun convert(from: Double): Double {
        return from / factorToFeet
    }

}