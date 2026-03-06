package nl.codingwithlinda.smartstep.features.steps_override_user.reset.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.core.di.DispatcherProvider
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class ResetStepsViewModel(
    private val dailyStepRepo: DailyStepRepo,
    private val currentStep: DailyStepCount,
    private val scope: CoroutineScope
): ViewModel() {

    fun reset() {
        scope.launch {

            with(currentStep) {
                val update = copy(stepCount = 0)
                println("--- RESETSTEPSVIEWMODEL --- update: $update")
                dailyStepRepo.saveDailyStepCountUserOverride(
                    currentStep.dateYYYYMMDD, 0
                )
            }
        }
    }
}