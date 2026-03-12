package nl.codingwithlinda.unit_conversion.domain


sealed interface UnitSystems {
    object IMPERIAL : UnitSystems
    object SI : UnitSystems
}


