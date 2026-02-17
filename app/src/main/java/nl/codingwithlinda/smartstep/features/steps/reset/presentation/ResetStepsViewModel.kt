package nl.codingwithlinda.smartstep.features.steps.reset.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.domain.repo.DailyStepRepo

class ResetStepsViewModel(
    private val dailyStepRepo: DailyStepRepo
): ViewModel() {


    fun reset(){
        SmartStepApplication.applicationScope.launch {
            DailyStepCountCreator.create(0).also {
                dailyStepRepo.saveStepCount(it)
            }
        }
    }
}