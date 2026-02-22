package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import android.util.Log.i
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.model.DailyStepGoalEntity
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import nl.codingwithlinda.smartstep.core.domain.util.factories.DateTimeHelper

fun DailyStepGoalEntity.toDomain(): DailyStepGoal{
    val localDate = DateTimeHelper.toDateYYYYMMDD(this.date)
    return DailyStepGoal(
        YYYY = localDate.YYYY,
        MM = localDate.MM,
        DD = localDate.DD,
        goal = goal
    )

}

fun DailyStepGoal.toGoalEntity(userId: String): DailyStepGoalEntity{
    return DailyStepGoalEntity(
        date = this.epochDay,
        goal = goal,
        userId = userId
    )
}