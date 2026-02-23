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
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabaseCreator
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import java.time.LocalDate

class StepTrackerService : Service() {

    private lateinit var stepTracker: StepTracker
    private lateinit var dailyStepRepoRoomImpl: DailyStepRepoRoomImpl
    private lateinit var notificationManager: NotificationManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager =  getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val db = SmartStepRoomDatabaseCreator.getInstance(applicationContext)
        dailyStepRepoRoomImpl = DailyStepRepoRoomImpl(
            dailyStepGoalDao = db.dailyStepGoalDao,
            dailyStepCountDao = db.dailyStepCountDao,
            userId = "todo"
        )
        stepTracker = SmartStepApplication.stepTracker

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

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SmartStep is running")
            .setContentText("SmartStep is tracking your steps")
            .setSmallIcon(R.drawable.splash_icon)
            .setOngoing(true)

        if(notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(1, notification.build())
        }
        startForeground(1, notification.build())

        stepTracker.stepsTaken.onEach { step ->
            if(step.stepCount == 0) return@onEach

            val today = LocalDate.of(step.YYYY, step.MM, step.DD).toEpochDay()
            //check if we have a baseline
            val baseline = dailyStepRepoRoomImpl.getDailyStepCountBaselineForDate(today)
            if(baseline == null){
                dailyStepRepoRoomImpl.saveDailyStepCountBaseline(step)
            }
            baseline?.let {baseline ->
                val difference = step.stepCount - baseline.stepCount
                dailyStepRepoRoomImpl.addStepCountToToday(
                    DailyStepCountCreator.create(
                        count = difference,
                        date = today
                    )
                )
            }

        }.launchIn(serviceScope)


    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stepTracker.stop()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SmartStep stopped running")
            .setContentText("SmartStep is no longer tracking your steps")
            .setSmallIcon(R.drawable.splash_icon)
            .setOngoing(true)

        if(notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(2, notification.build())
        }
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