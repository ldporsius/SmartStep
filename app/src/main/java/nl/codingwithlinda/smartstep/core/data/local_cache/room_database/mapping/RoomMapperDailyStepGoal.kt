package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepGoalEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal

fun DailyStepGoalEntity.toDomain(): DailyStepGoal{
    return DailyStepGoal(
        date = date,
        goal = goal
    )

}

fun DailyStepGoal.toEntity(userId: String): DailyStepGoalEntity{
    return DailyStepGoalEntity(
        date = date,
        goal = goal,
        userId = userId
    )
}