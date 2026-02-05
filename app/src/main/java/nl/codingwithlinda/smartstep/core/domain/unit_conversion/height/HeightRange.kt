package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height


val heightsCm = IntRange(100, 250).toList()
val heightsFeet = IntRange(0, 8).toList()
val heightsInches = IntRange(0,11).toList()

val minHeightCm = heightsCm.first()
val maxHeightCm = heightsCm.last()
val minHeightFeet = heightsFeet.first()
val maxHeightFeet = heightsFeet.last()
val minHeightInches = heightsInches.first()
val maxHeightInches = heightsInches.last()