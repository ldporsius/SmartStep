package nl.codingwithlinda.smartstep.features.statistics.domain.calculations

fun calculateStepLength(
    personsHeightCm: Int
): Double{
    return personsHeightCm * 0.415
}

fun calculateDistanceCm(
    personsHeightCm: Int,
    stepsTaken: Int
): Double{
    return calculateStepLength(personsHeightCm) * stepsTaken
}