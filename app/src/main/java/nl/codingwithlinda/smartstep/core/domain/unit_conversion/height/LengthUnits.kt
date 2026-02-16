package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems

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