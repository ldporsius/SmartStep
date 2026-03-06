package nl.codingwithlinda.smartstep.tests.util

import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.core.domain.util.UiText
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi

val fakeStatistics =
    StatisticsUi(
        distance = UiText.DynamicText("0"),
        energy = UiText.StringResourceText(R.string.energy, "0" , "kcal"),
        time = UiText.DynamicText("0")
)