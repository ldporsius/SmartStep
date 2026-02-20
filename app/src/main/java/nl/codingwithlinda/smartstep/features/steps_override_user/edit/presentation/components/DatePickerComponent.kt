package nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.codingwithlinda.smartstep.features.main.presentation.common.CommonNumberPicker
import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.DatePicker
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DateYYYYMMDD
import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.months
import nl.codingwithlinda.smartstep.features.steps_override_user.domain.model.years
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.state.EditStepAction

@Composable
fun DatePickerComponent(
    selectedDate: DateYYYYMMDD,
    action: (EditStepAction) -> Unit,
    years: List<Int>,
    months: List<Int>,
    daysInMonth: List<Int>,
    modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        BoxWithConstraints(

        ) {
            val oneThird = maxWidth / 3

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CommonNumberPicker(
                    label = "",
                    values = years,
                    selectedGoal = selectedDate.YYYY,
                    onGoalSelected = {
                        action(EditStepAction.InputYear(it))
                    },
                    modifier = Modifier.width(oneThird)
                )
                CommonNumberPicker(
                    label = "",
                    values = months,
                    selectedGoal = selectedDate.MM,
                    onGoalSelected = {
                        action(EditStepAction.InputMonth(it))
                    },
                    modifier = Modifier.width(oneThird)
                )
                CommonNumberPicker(
                    label = "",
                    values = daysInMonth,
                    selectedGoal = selectedDate.DD,
                    onGoalSelected = {
                        action(EditStepAction.InputDay(it))
                    },
                    modifier = Modifier.width(oneThird)
                )
            }
        }

        CommonDialogButtonRow(
            onDismiss = {
                action(EditStepAction.DismissDatePicker)
            },
            onSave = {
                action(EditStepAction.DismissDatePicker)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewDatePickerComponent() {
    val selectedDate = DateYYYYMMDD(2023, 1, 31)
    DatePickerComponent(
        selectedDate = selectedDate,
        action = {},
        years = years.toList(),
        months = months.toList(),
        daysInMonth = DatePicker(selectedDate.YYYY).daysInMonth(selectedDate.MM).toList()
        ,
        modifier = Modifier.fillMaxSize()
    )

}