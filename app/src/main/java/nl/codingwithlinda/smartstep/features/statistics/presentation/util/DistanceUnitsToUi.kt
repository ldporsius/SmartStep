package nl.codingwithlinda.smartstep.features.statistics.presentation.util

import nl.codingwithlinda.unit_conversion.data.distance.CM
import nl.codingwithlinda.unit_conversion.data.distance.Distance
import nl.codingwithlinda.unit_conversion.data.distance.KM
import nl.codingwithlinda.unit_conversion.data.distance.METER
import nl.codingwithlinda.unit_conversion.data.distance.MILE

fun Distance.toUi(): String{
   return when(this) {
        CM -> "cm"
        KM -> "km"
        METER -> "m"
        MILE -> "mi"
    }
}