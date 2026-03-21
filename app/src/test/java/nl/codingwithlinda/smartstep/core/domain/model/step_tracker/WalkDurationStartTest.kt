package nl.codingwithlinda.smartstep.core.domain.model.step_tracker

import kotlinx.coroutines.test.runTest
import nl.codingwithlinda.smartstep.FakeWalkDurationRepo
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class WalkDurationStartTest {

    val repo = FakeWalkDurationRepo()

    val startTimes = List(10){minute ->
        LocalDateTime.of(2023,1,1,12,minute, 0,).toEpochSecond(ZoneOffset.UTC)
    }

    val endTimes = List(10){minute ->
        LocalDateTime.of(2023,1,1,12,minute + 10, 0,).toEpochSecond(ZoneOffset.UTC)
    }


    @Test
    fun `test walk duration flow`() = runTest{

        startTimes.forEach {
            repo.saveWalkDurationStart(
                it
            )
        }

        println("start times saved")

        endTimes.forEach {
            repo.saveWalkDurationEnd(
                it
            )
        }

        println("end times saved")


        repo.sessions.collect {sessions ->

            sessions.forEach { session ->
                println("session: ${session.id}")

                val start = session.start.timestamp
                val end = session.end?.timestamp

                if (end != null) {
                    val startTime = LocalDateTime.ofEpochSecond(start, 0, ZoneOffset.UTC)
                    val endTime = LocalDateTime.ofEpochSecond(end, 0, ZoneOffset.UTC)


                    println("start: ${startTime.minute}:${startTime.second}, end: ${endTime.minute}:${endTime.second}")
                    println("durationSeconds ${end - start}")
                }
            }

        }
    }



}