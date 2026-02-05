package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt


val weightRangeKg = IntRange(25, 200).toList()

val minWeightPounds = ceil(weightRangeKg.first() * WeightUnitConverter.kgToPounds).toInt()
val maxWeightPounds = floor(weightRangeKg.last() * WeightUnitConverter.kgToPounds).toInt()
val weightRangePounds = IntRange(minWeightPounds, maxWeightPounds).toList()