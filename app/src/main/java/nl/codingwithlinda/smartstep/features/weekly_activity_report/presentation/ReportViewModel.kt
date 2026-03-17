package nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetAction
import nl.codingwithlinda.smartstep.features.weekly_activity_report.presentation.interaction.ReportTargetUiState

class ReportViewModel: ViewModel() {


    private val _uiState = MutableStateFlow(ReportTargetUiState())
    val uiState = _uiState.asStateFlow()


    fun onAction(action: ReportTargetAction) {
        when(action){
            is ReportTargetAction.SetTargetAction -> {
                _uiState.update {
                    it.copy(selectedTarget = action.target)
                }
            }
        }
    }
}