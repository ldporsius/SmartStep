package nl.codingwithlinda.unit_conversion.data.height

import nl.codingwithlinda.unit_conversion.domain.UnitSystems

enum class LengthUnits{
    CM {
        override val system: UnitSystems
            get() = UnitSystems.SI
    },
    FEET_INCHES {
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL
    }
    ;

    abstract val system: UnitSystems
}