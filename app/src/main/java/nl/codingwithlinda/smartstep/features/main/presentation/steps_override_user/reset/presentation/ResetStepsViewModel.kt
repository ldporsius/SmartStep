package nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class ResetStepsViewModel(
    private val dailyStepRepo: DailyStepRepo,
    private val currentStep: DailyStepCount,
    private val scope: CoroutineScope
): ViewModel() {

    fun reset() {
        scope.launch {
            println("--- RESETSTEPSVIEWMODEL --- reset")
            dailyStepRepo.saveDailyStepCountUserOverride(
                currentStep.dateYYYYMMDD, 0
            )
        }
    }
}