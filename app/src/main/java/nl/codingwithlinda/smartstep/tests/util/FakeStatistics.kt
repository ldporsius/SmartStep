package nl.codingwithlinda.smartstep.tests.util

import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi

val fakeStatistics =
    StatisticsUi(
        distance = UiText.DynamicText("42km"),
        energy = UiText.StringResourceText(R.string.energy, "215" , "kcal"),
        time = UiText.DynamicText("1min")
)