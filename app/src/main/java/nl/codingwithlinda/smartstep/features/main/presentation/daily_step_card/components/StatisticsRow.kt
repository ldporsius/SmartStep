package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.features.statistics.presentation.components.StatisticsItem
import nl.codingwithlinda.smartstep.features.statistics.presentation.model.StatisticsUi

@Composable
fun StatisticsRow(
    statisticsUi: StatisticsUi,
    iconModifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        StatisticsItem(
            icon = R.drawable.location_track,
            value = statisticsUi.distance,
            modifier = iconModifier.then(
                Modifier.semantics(){
                    contentDescription = "distance"
                }
            )
        )

        StatisticsItem(
            icon = R.drawable.weight_diet,
            value = statisticsUi.energy,
            modifier = iconModifier.then(
                Modifier.semantics(){
                    contentDescription = "energy"
                }
            )
        )
        StatisticsItem(
            icon = R.drawable.time_clock,
            value = statisticsUi.time,
            modifier = iconModifier.then(
                Modifier.semantics(){
                    contentDescription = "time"
                }
            )
        )

    }
}