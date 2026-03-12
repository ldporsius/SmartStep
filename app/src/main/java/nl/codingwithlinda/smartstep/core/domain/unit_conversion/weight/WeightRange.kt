package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import nl.codingwithlinda.unit_conversion.data.weight.KG
import nl.codingwithlinda.unit_conversion.data.weight.KGWeight
import nl.codingwithlinda.unit_conversion.data.weight.LBS
import nl.codingwithlinda.unit_conversion.data.weight.LBSWeight
import nl.codingwithlinda.unit_conversion.data.weight.convertWeight
import kotlin.collections.component1
import kotlin.collections.get
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt


private val kgToPoundsFactor: Double
    get() = 2.20462

val weightRangeKg = IntRange(25, 200).toList()

val minWeightPounds = ceil(weightRangeKg.first() * kgToPoundsFactor).toInt()
val maxWeightPounds = floor(weightRangeKg.last() * kgToPoundsFactor).toInt()
val weightRangePounds = IntRange(minWeightPounds, maxWeightPounds).toList()

val kgToPounds = weightRangePounds.map {
    val pound = LBSWeight(it.toDouble())
    val kg = convertWeight(pound, KG)

    it to kg
}.groupBy { (pounds, kg) ->
    kg.weight.roundToInt()
}.mapValues{ (i, pairs) ->
    pairs.map { it.first }
}

fun fromPreviousPounds(kg: KGWeight, pounds: Int): LBSWeight{

    val correspondingPounds: List<Int> = kgToPounds[kg.weight.roundToInt()] ?:emptyList()

    println("--- KG --- correspondingPounds: $correspondingPounds")

    if (pounds in correspondingPounds) {
        println("--- KG --- returning previous pounds: $pounds")
        return LBSWeight(pounds.toDouble())
    }
    val converted = convertWeight(kg, LBS)

    return converted as LBSWeight
}