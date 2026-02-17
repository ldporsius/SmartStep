package nl.codingwithlinda.smartstep.features.steps.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.EditStepsViewModel
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.components.DatePickerComponent
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.components.EditStepsDialog
import nl.codingwithlinda.smartstep.features.steps.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.features.steps.reset.presentation.ResetStepsDialog
import nl.codingwithlinda.smartstep.features.steps.reset.presentation.ResetStepsViewModel

@Composable
fun StepsNavActionDecorator(
    editStepsViewModel: EditStepsViewModel
) {

  /*
*/
    var action: StepNavAction by remember { mutableStateOf(StepNavAction.NA) }

    ObserveAsEvents(StepNavActionHandler.actions) { _action ->
        action = _action
    }

    when(action){
        StepNavAction.NA -> Unit
        StepNavAction.SHOW_DATE_PICKER -> {
            Dialog(
                onDismissRequest = {
                    StepNavActionHandler.handleAction(
                        StepNavAction.EDIT_STEPS
                    )
                }
            ) {
                Surface() {
                    DatePickerComponent(
                        selectedDate = editStepsViewModel.dateYYYYMMDD.collectAsStateWithLifecycle().value,
                        action = editStepsViewModel::onAction,
                        years = editStepsViewModel.yearRange,
                        months = editStepsViewModel.monthRange,
                        daysInMonth = editStepsViewModel.dayRange.collectAsStateWithLifecycle().value,
                        modifier = Modifier
                    )
                }
            }
        }
        StepNavAction.EDIT_STEPS -> {
            Dialog(
                onDismissRequest = {
                    StepNavActionHandler.handleAction(
                        StepNavAction.NA
                    )
                }
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                ) {
                    EditStepsDialog(
                        onDismiss = {
                            StepNavActionHandler.handleAction(
                                StepNavAction.NA
                            )
                        },
                        action = editStepsViewModel::onAction,
                        dateSelected = editStepsViewModel.dateSelectedAsString.collectAsStateWithLifecycle().value,
                        numSteps = editStepsViewModel.steps.collectAsStateWithLifecycle().value,
                        onSave = {
                            editStepsViewModel.onAction(EditStepAction.Save)
                        },
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
        StepNavAction.RESET_STEPS -> {
            val resetStepsViewModel = viewModel<ResetStepsViewModel>(
                factory = viewModelFactory {

                    initializer {
                        ResetStepsViewModel(
                            dailyStepRepo = SmartStepApplication.dailyStepRepo
                        )
                    }
                }
            )
           Dialog(
               onDismissRequest = {
                   StepNavActionHandler.handleAction(
                       StepNavAction.NA
                   )
               }
           ) {
               Surface(
                   shape = MaterialTheme.shapes.medium,
                   modifier = Modifier.fillMaxWidth()
               ) {
                   ResetStepsDialog(
                       onDismiss = {
                           StepNavActionHandler.handleAction(
                               StepNavAction.NA
                           )
                       },
                       onReset = {
                           resetStepsViewModel.reset()
                       },
                       modifier = Modifier.fillMaxWidth().padding(16.dp)
                   )
               }
           }
        }
    }

}