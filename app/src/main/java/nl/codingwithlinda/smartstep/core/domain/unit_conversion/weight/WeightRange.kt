package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import android.util.Log.i
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.Weights
import kotlin.collections.component1
import kotlin.math.ceil
import kotlin.math.floor



val kgToGramFactor: Double
    get() = 1000.0

val kgToPoundsFactor: Double
    get() = 2.20462

val weightRangeKg = IntRange(25, 200).toList()


val minWeightPounds = ceil(weightRangeKg.first() * kgToPoundsFactor).toInt()
val maxWeightPounds = floor(weightRangeKg.last() * kgToPoundsFactor).toInt()
val weightRangePounds = IntRange(minWeightPounds, maxWeightPounds).toList()

val kgToPounds = weightRangePounds.map {
    val pound = WeightUnits.LBS(it)
    val kg = pound.convert<WeightUnits.KG>(Weights.KG)

    it to kg.kg
}.groupBy { (pounds, kg) ->
    kg
}.mapValues{ (i, pairs) ->
    pairs.map { it.first }
}