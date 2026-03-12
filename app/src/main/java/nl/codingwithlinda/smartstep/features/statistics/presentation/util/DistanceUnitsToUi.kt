package nl.codingwithlinda.smartstep.features.statistics.presentation.util

import nl.codingwithlinda.unit_conversion.data.CM
import nl.codingwithlinda.unit_conversion.data.Distance
import nl.codingwithlinda.unit_conversion.data.KM
import nl.codingwithlinda.unit_conversion.data.METER
import nl.codingwithlinda.unit_conversion.data.MILE

fun Distance.toUi(): String{
   return when(this) {
        CM -> "cm"
        KM -> "km"
        METER -> "m"
        MILE -> "mi"
    }
}