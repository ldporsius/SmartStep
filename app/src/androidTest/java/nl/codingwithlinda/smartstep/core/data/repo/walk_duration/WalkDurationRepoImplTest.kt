package nl.codingwithlinda.smartstep.core.data.repo.walk_duration

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepDatabase
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.WalkSession
import org.junit.Assert.*
import org.junit.Test
import kotlin.jvm.java
import kotlin.time.Duration.Companion.minutes

class WalkDurationRepoImplTest {

    val context = ApplicationProvider.getApplicationContext<Application>()
    val db = Room.inMemoryDatabaseBuilder(
        context,
        SmartStepDatabase::class.java,
        ).build()

    val repo = WalkDurationRepoImpl(
        dao = db.walkSessionDao
    )

    @Test
    fun testWalkDurationRepoImpl(): Unit = runBlocking{
        val timestamp = 1.minutes.inWholeMilliseconds
        repo.saveWalkDurationStart(timestamp)
        repo.saveWalkDurationEnd(timestamp.plus(1.minutes.inWholeMilliseconds))
        repo.saveWalkDurationStart(timestamp.plus(2.minutes.inWholeMilliseconds))
        repo.saveWalkDurationStart(timestamp.plus(3.minutes.inWholeMilliseconds))


        val result = mutableListOf<WalkSession>()
        val job = launch {
            repo.sessions.onEach{ sessions ->
                sessions.onEach {
                    println("--- $it")
                    result.add(it)
                }
            }.collect()
        }

        delay(1000)

        job.cancelAndJoin()

        result
            .sortedBy {
                it.id
            }
            .onEach {
            println("--- ${it.start.timestamp} - ${it.end?.timestamp}")
        }.also {
                assertThat(it.size).isEqualTo(3)
                assertThat(it[0].start.timestamp).isEqualTo(timestamp)
            }
    }
}