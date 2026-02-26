package nl.codingwithlinda.smartstep.features.steps_override_user.navigation

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
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.EditStepsViewModel
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.components.DatePickerComponent
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.components.EditStepsDialog
import nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.state.EditStepAction
import nl.codingwithlinda.smartstep.features.steps_override_user.reset.presentation.ResetStepsDialog
import nl.codingwithlinda.smartstep.features.steps_override_user.reset.presentation.ResetStepsViewModel

@Composable
fun UserOverrideStepsNavActionDecorator(
   currentStep: DailyStepCount
) {

    val editStepsViewModel: EditStepsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                EditStepsViewModel(
                    dailyStepRepo = SmartStepApplication.dailyStepRepo,

                )
            }
        }
    )
    var action: StepNavAction by remember { mutableStateOf(StepNavAction.NA) }

    ObserveAsEvents(StepNavActionHandler.actions) { _action ->

        println("user override steps nav action decorator Observed Action: $_action")
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
                            StepNavActionHandler.handleAction(
                                StepNavAction.NA
                            )
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
                            dailyStepRepo = SmartStepApplication.dailyStepRepo,
                            currentStep = currentStep,
                            scope = SmartStepApplication.applicationScope
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
                   modifier = Modifier
               ) {
                   ResetStepsDialog(
                       onDismiss = {
                           StepNavActionHandler.handleAction(
                               StepNavAction.NA
                           )
                       },
                       onReset = {
                           resetStepsViewModel.reset()
                           StepNavActionHandler.handleAction(
                               StepNavAction.NA
                           )
                       },
                       modifier = Modifier.fillMaxWidth().padding(16.dp)
                   )
               }
           }
        }
    }

}