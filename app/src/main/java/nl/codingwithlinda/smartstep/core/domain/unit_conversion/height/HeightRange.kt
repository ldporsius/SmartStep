package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import nl.codingwithlinda.unit_conversion.data.lenght.FeetInchesUnitConverter
import nl.codingwithlinda.unit_conversion.data.lenght.lenght_defs.FeetInches
import kotlin.math.roundToInt


val heightsFeet = IntRange(0, 7).toList()
val heightsInches = IntRange(0,11).toList()


val minHeightFeet = heightsFeet.first()
val maxHeightFeet = heightsFeet.last()
val minHeightInches = heightsInches.first()
val maxHeightInches = heightsInches.last()


fun maxHeightCm(): Int {
    return FeetInches(maxHeightFeet, maxHeightInches).let {
        FeetInchesUnitConverter().toCm(it).value.roundToInt()
    }
}

val heightsCm = IntRange(100, maxHeightCm()).toList()