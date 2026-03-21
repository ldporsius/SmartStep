package nl.codingwithlinda.smartstep.core.data.repo.walk_duration

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepDatabase
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
        val timestamp = System.currentTimeMillis()
        repo.saveWalkDurationStart(timestamp)

        repo.saveWalkDurationEnd(timestamp.plus(1.minutes.inWholeMilliseconds))

        val job = launch {
            repo.sessions.toList().onEach { sessions ->
                sessions.onEach {
                    println("--- $it")
                }
            }
        }

        job.cancelAndJoin()
    }
}