package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import nl.codingwithlinda.unit_conversion.domain.UnitSystems

enum class Weights(val system: UnitSystems){
    GRAMS(UnitSystems.SI),
    KG(UnitSystems.SI),
    LBS(UnitSystems.IMPERIAL)
}