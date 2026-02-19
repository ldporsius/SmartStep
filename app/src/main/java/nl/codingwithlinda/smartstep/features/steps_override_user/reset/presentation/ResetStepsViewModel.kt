package nl.codingwithlinda.smartstep.features.steps_override_user.reset.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class ResetStepsViewModel(
    private val dailyStepRepo: DailyStepRepo
): ViewModel() {


    fun reset(){
        SmartStepApplication.applicationScope.launch {

           DailyStepCountCreator.getTodayAsSeconds().let {
               dailyStepRepo.getStepCountForDate(it)
           }?.also {
               dailyStepRepo.saveDailyStepCountBaseline(it)
           }
            DailyStepCountCreator.create(0).also {
                dailyStepRepo.saveStepCount(it)
            }
        }
    }
}