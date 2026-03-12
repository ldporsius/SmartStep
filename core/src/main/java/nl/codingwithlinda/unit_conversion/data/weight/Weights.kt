package nl.codingwithlinda.unit_conversion.data.weight

import nl.codingwithlinda.unit_conversion.domain.UnitSystems

enum class Weights(val system: UnitSystems){
    GRAMS(UnitSystems.SI),
    KG(UnitSystems.SI),
    LBS(UnitSystems.IMPERIAL)
}