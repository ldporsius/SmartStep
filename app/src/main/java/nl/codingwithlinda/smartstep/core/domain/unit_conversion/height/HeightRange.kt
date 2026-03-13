package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import nl.codingwithlinda.unit_conversion.data.lenght.FeetInches


val heightsFeet = IntRange(0, 7).toList()
val heightsInches = IntRange(0,11).toList()


val minHeightFeet = heightsFeet.first()
val maxHeightFeet = heightsFeet.last()
val minHeightInches = heightsInches.first()
val maxHeightInches = heightsInches.last()


val maxHeightCm = FeetInches(maxHeightFeet, maxHeightInches).valueCm.toInt()


val heightsCm = IntRange(100, maxHeightCm).toList()
val minHeightCm = heightsCm.first()