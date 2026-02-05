package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import kotlin.math.roundToInt


val weightRangeKg = IntRange(25, 200)
val weightRangePounds = weightRangeKg.map {
    (it * WeightUnitConverter.kgToPounds).roundToInt()

}