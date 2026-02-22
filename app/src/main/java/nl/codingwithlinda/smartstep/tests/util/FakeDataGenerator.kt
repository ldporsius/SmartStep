package nl.codingwithlinda.smartstep.tests.util

import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepCount
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import java.time.LocalDate
import java.time.LocalDateTime


val testDate = LocalDateTime.of(2026,2,21,0,0,0)
val fakeSteps = List(10){
    DailyStepCount(
        YYYY = 2026,
        MM = 2,
        DD = 28 - it,
        stepCount = it
    )
}

val fakeGoals = List(10){
    DailyStepGoal(
        YYYY = 2026,
        MM = 2,
        DD = 28 - it,
        goal = 100
    )
}