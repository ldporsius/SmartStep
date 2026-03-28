package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design_system.form_factors.ScreenForm
import nl.codingwithlinda.smartstep.design_system.form_factors.screenFormHelper
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownUi

@Composable
fun WeeklyBreakdownList(
    modifier: Modifier = Modifier,
    weekItems: List<WeeklyBreakdownUi>,
) {

    val screen = screenFormHelper()

    val numColumns = when(screen.form) {
        ScreenForm.PHONE -> 1
        ScreenForm.TABLET -> 2
        ScreenForm.DESKTOP -> 4
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(numColumns),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
        ) {
        items(weekItems){item ->
            WeeklyBreakdownItem(
                modifier = Modifier.width(296.dp),
                item
            )
        }
    }
}