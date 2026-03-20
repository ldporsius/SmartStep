package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import nl.codingwithlinda.smartstep.core.presentation.util.UiText
import nl.codingwithlinda.smartstep.design_system.ui.theme.SmartStepTheme
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownIcon
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownStatus
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.model.WeeklyBreakdownUi

@Composable
fun WeeklyBreakdownItem(
    modifier: Modifier = Modifier,
    uiState: WeeklyBreakdownUi
) {

    val textColor = when(uiState.status){
        WeeklyBreakdownStatus.FINISHED -> MaterialTheme.colorScheme.primary
        WeeklyBreakdownStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        WeeklyBreakdownStatus.NOT_STARTED -> MaterialTheme.colorScheme.surfaceDim
    }
    OutlinedCard(
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.width(480.dp).padding(16.dp)
        ) {
            val (
                day,
                value,
                unit,
                state,
                label
            ) = createRefs()

            Text(uiState.dayName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.constrainAs(day){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                color = textColor
            )

            Text(uiState.value.asString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.constrainAs(value){
                    top.linkTo(day.bottom)
                    start.linkTo(parent.start)
                },
                color = textColor
            )
            Text(uiState.unit.asString(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.constrainAs(unit){
                    bottom.linkTo(value.baseline)
                    start.linkTo(value.end, margin = 4.dp)
                }
            )

            uiState.status.WeeklyBreakdownIcon(
                modifier = Modifier.constrainAs(state){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            )
            Text(uiState.label.asString(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.constrainAs(label) {
                    top.linkTo(state.bottom, margin = 4.dp)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWeeklyBreakdownItem() {
    SmartStepTheme() {
        WeeklyBreakdownItem(
            modifier = Modifier.width(480.dp),
            uiState = WeeklyBreakdownUi(
                dayName = "Monday",
                value = UiText.DynamicText("100"),
                unit = UiText.DynamicText("steps"),
                status = WeeklyBreakdownStatus.FINISHED
            )
        )
    }
}