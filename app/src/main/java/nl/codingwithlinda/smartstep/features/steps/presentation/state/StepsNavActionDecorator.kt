package nl.codingwithlinda.smartstep.features.steps.presentation.state

import android.R.attr.action
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.steps.presentation.EditStepsDialog
import nl.codingwithlinda.smartstep.features.steps.presentation.EditStepsViewModel

@Composable
fun StepsNavActionDecorator(modifier: Modifier = Modifier) {

    val editStepsViewModel = viewModel<EditStepsViewModel>(
        factory = viewModelFactory {
            initializer {
                EditStepsViewModel(
                    dailyStepRepo = SmartStepApplication.dailyStepRepo
                )
            }
        }
    )

    var action: StepNavAction by remember { mutableStateOf(StepNavAction.NA) }

    ObserveAsEvents(StepNavActionHandler.actions) { _action ->
        action = _action
    }

    when(action){
        StepNavAction.NA -> Unit
        StepNavAction.EDIT_STEPS -> {
            Dialog(
                onDismissRequest = {
                    StepNavActionHandler.handleAction(
                        StepNavAction.NA
                    )
                }
            ) {
                Surface() {
                    EditStepsDialog(
                        onDismiss = {
                            StepNavActionHandler.handleAction(
                                StepNavAction.NA
                            )
                        },
                        action = editStepsViewModel::onAction,
                        dateSelected = editStepsViewModel.dateSelected.collectAsStateWithLifecycle().value,
                        numSteps = editStepsViewModel.steps.collectAsStateWithLifecycle().value,
                        onSave = { },
                        modifier = Modifier
                    )
                }
            }
        }
        StepNavAction.RESET_STEPS -> {
            Box() {
                Text("Reset steps")
            }
        }
    }

}