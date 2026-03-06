package nl.codingwithlinda.smartstep.features.statistics.presentation.util

import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.CM
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.Distance
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.KM
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.METER
import nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion.MILE

fun Distance.toUi(): String{
   return when(this) {
        CM -> "cm"
        KM -> "km"
        METER -> "m"
        MILE -> "mi"
    }
}