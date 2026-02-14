package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import kotlin.math.ceil
import kotlin.math.floor


val weightRangeKg = IntRange(25, 200).toList()

val weightRangeGrams = weightRangeKg.map {
    GroupedWeightRange(
        indicator = it,
        range = (it * 1000)..((it + 1) * 1000)
    )
}


val minWeightPounds = ceil(weightRangeKg.first() * WeightUnitConverter.kgToPounds).toInt()
val maxWeightPounds = floor(weightRangeKg.last() * WeightUnitConverter.kgToPounds).toInt()
val weightRangePounds = IntRange(minWeightPounds, maxWeightPounds).toList()


data class GroupedWeightRange(
    val indicator: Int,
    val range: IntRange
)