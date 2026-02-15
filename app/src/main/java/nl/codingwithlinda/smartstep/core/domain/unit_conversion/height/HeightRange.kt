package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import kotlin.math.roundToInt


val heightsFeet = IntRange(0, 7).toList()
val heightsInches = IntRange(0,11).toList()


val minHeightFeet = heightsFeet.first()
val maxHeightFeet = heightsFeet.last()
val minHeightInches = heightsInches.first()
val maxHeightInches = heightsInches.last()


val maxHeightCm = HeightUnitConverter.toSI(maxHeightFeet, maxHeightInches).roundToInt()


val heightsCm = IntRange(100, maxHeightCm).toList()
val minHeightCm = heightsCm.first()