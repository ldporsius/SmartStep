package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabaseCreator
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.mapping.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl

class StepTrackerService : Service() {

    lateinit var stepTracker: StepTrackerImpl
    lateinit var dailyStepRepoRoomImpl: DailyStepRepoRoomImpl
    val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val db = SmartStepRoomDatabaseCreator.getInstance(applicationContext)
        dailyStepRepoRoomImpl = DailyStepRepoRoomImpl(
            dailyStepGoalDao = db.dailyStepGoalDao,
            dailyStepCountDao = db.dailyStepCountDao,
            userId = "todo"
        )
        stepTracker = StepTrackerImpl.getInstance(this, serviceScope)
        stepTracker.initialize()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun start(){
        stepTracker.start()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SmartStep is running")
            .setContentText("SmartStep is tracking your steps")
            .setSmallIcon(R.drawable.splash_icon)
            .setOngoing(true)
        notificationManager.notify(1, notification.build())

        stepTracker.stepsTaken.onEach {
            notification.setContentText("Steps taken: $it")
            notificationManager.notify(1, notification.build())

            DailyStepCountCreator.create(1).also {
                dailyStepRepoRoomImpl.saveStepCount(it)
            }

        }.launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stepTracker.stop()
        stopSelf()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object{
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val CHANNEL_ID = "smartstep_channel"
    }
}