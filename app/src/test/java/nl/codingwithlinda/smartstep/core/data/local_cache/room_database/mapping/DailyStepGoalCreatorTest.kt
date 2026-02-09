package nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping

import android.util.Log.i
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.DailyStepGoal
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import kotlin.time.Duration.Companion.days

class DailyStepGoalCreatorTest {

    @Test
    fun `test two moments in time are the same day`(){
        val oneOClock = Instant.now()
        val twoOClock = oneOClock.plusSeconds(3600)
        val d1 = DailyStepGoalCreator.create(
            goal = 1000,
            date = oneOClock.toEpochMilli()
        )
        val d2 = DailyStepGoalCreator.create(
            goal = 1000,
            date = twoOClock.toEpochMilli()
        )

        println(d1)
        println(d2)

        assertEquals(d1.date, d2.date)

    }

    @Test
    fun `test todays goal`(){
        val today = Instant.now()
        val laterToday = today.plus(Duration.ofHours(2))
        val tomorrow = today.plus(Duration.ofDays(1))
        val yesterday = today.minus(Duration.ofDays(1))


        val goals = listOf(
            yesterday, today, laterToday, tomorrow
        ).mapIndexed {i, it ->
            DailyStepGoalCreator.create(
                goal = 1000 + i,
                date = it.toEpochMilli()
            )
        }

        val todayGoal = DailyStepGoalCreator.getTodaysGoal(goals, today.toEpochMilli())

        println(todayGoal)
        assertEquals(todayGoal?.goal, 1002)
    }
    }
