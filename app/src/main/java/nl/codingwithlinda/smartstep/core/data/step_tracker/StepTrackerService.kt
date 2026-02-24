package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.di.AndroidDispatcherProvider
import nl.codingwithlinda.smartstep.core.data.local_cache.room_database.SmartStepRoomDatabaseCreator
import nl.codingwithlinda.smartstep.core.data.repo.DailyStepRepoRoomImpl
import nl.codingwithlinda.smartstep.core.domain.model.step_tracker.StepTracker
import nl.codingwithlinda.smartstep.core.domain.util.factories.DailyStepCountCreator
import nl.codingwithlinda.smartstep.features.statistics.data.StatisticsManagerImpl
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import kotlin.math.roundToInt

class StepTrackerService : Service() {

    private lateinit var stepTracker: StepTracker
    private lateinit var dailyStepRepoRoomImpl: DailyStepRepoRoomImpl
    private lateinit var notificationManager: NotificationManager

    private lateinit var statisticsManager: StatisticsManager

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

        statisticsManager = StatisticsManagerImpl(
            userSettingsRepo = SmartStepApplication.userSettingsRepo,
            dailyStepRepo = dailyStepRepoRoomImpl,
            walkDurationRepo = SmartStepApplication.walkDurationRepo,
            dispatcherProvider = AndroidDispatcherProvider()
        )
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

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val contentSmall = RemoteViews(packageName, R.layout.notification_small)
        val contentLarge = RemoteViews(packageName, R.layout.notification_large)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.footprints)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(contentSmall)
            .setCustomBigContentView(contentLarge)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(false)

        startForeground(1, notification.build())

        stepTracker.stepsTaken.onEach { step ->
            if(step.stepCount == 0) return@onEach

            //check if we have a baseline
            val baseline = dailyStepRepoRoomImpl.getDailyStepCountBaselineForDate(step.dateYYYYMMDD)
            if(baseline == null){
                dailyStepRepoRoomImpl.saveDailyStepCountBaseline(step)
            }
            baseline?.let {baseline ->
                val difference = step.stepCount - baseline.stepCount
                dailyStepRepoRoomImpl.saveStepCount(
                    DailyStepCountCreator.create(
                        count = difference,
                        date = step.dateYYYYMMDD
                    )
                )
            }

        }.launchIn(serviceScope)

        combine(statisticsManager.stepsToday, statisticsManager.caloriesBurned, statisticsManager.progressTowardsGoal){steps, calories , progress->

            contentSmall.setTextViewText(R.id.step_count, steps.toString())
            contentSmall.setTextViewText(R.id.calories, calories.toString())
            contentSmall.setProgressBar(R.id.progress_bar, 100, (progress * 100).roundToInt(), false)

            contentLarge.setTextViewText(R.id.step_count, steps.toString())
            contentLarge.setTextViewText(R.id.calories, calories.toString())
            contentLarge.setProgressBar(R.id.progress_bar, 100, (progress * 100).roundToInt(), false)


            if(notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(1, notification.build())
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