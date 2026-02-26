package nl.codingwithlinda.smartstep.core.data.step_tracker

import android.app.Notification
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.MainActivity
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.features.statistics.domain.StatisticsManager
import kotlin.math.roundToInt

class StepTrackerService : Service() {

    private lateinit var notificationManager: NotificationManager

    private lateinit var statisticsManager: StatisticsManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager =  getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        statisticsManager = SmartStepApplication.statisticsManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }



    private fun createNotification(
        steps: Int,
        calories: Int,
        progress: Float
    ): Notification{
        val contentSmall = RemoteViews(packageName, R.layout.notification_small)
        val contentLarge = RemoteViews(packageName, R.layout.notification_large)

        contentSmall.setTextViewText(R.id.step_count, steps.toString())
        contentSmall.setTextViewText(R.id.calories, calories.toString())
        contentSmall.setProgressBar(
            R.id.progress_bar,
            100,
            (progress * 100).roundToInt(),
            false
        )

        contentLarge.setTextViewText(R.id.step_count, steps.toString())
        contentLarge.setTextViewText(R.id.calories, calories.toString())
        contentLarge.setProgressBar(
            R.id.progress_bar,
            100,
            (progress * 100).roundToInt(),
            false
        )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.footprints)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(contentSmall)
            .setCustomBigContentView(contentLarge)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)


        return notification.build()
    }

    private val notificationUpdater = Channel<SmartStepNotification>()
    private val _notificationInfo = MutableStateFlow<SmartStepNotification>(SmartStepNotification(0,0,0f))
    private fun notificationInfo() =  notificationUpdater.receiveAsFlow().map {
      info, ->

        println("--- STEP TRACKER SERVICE combined statistics --- steps: ${info.steps}, calories: ${info.calories} , progress: ${info.progress}")

        createNotification(
            steps = info.steps,
            calories = info.calories,
            progress = info.progress
        )
    }

    private fun start(){

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.footprints)
            .setContentTitle("Smart Step is tracking in the background")
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(false)

        startForeground(1, notification.build())


        serviceScope.launch {
            statisticsManager.stepsToday.collect {newSteps ->
                println("--- STEP TRACKER SERVICE --- steps: $newSteps")
               val update = _notificationInfo.updateAndGet {
                   it.copy(
                       steps = newSteps
                   )
               }
                notificationUpdater.send(update)
            }
        }

        serviceScope.launch {
            statisticsManager.caloriesBurned.collect {newCalories ->
                val update = _notificationInfo.updateAndGet {
                    it.copy(
                        calories = newCalories
                    )
                }
                notificationUpdater.send(update)
            }
        }

        serviceScope.launch {
            statisticsManager.progressTowardsGoal.collect {progress ->
                val update = _notificationInfo.updateAndGet {
                    it.copy(
                        progress = progress
                    )
                }
                notificationUpdater.send(update)
            }
        }

        serviceScope.launch {
            notificationInfo().collectLatest {
                if (notificationManager.areNotificationsEnabled()) {
                    notificationManager.notify(1, it)
                }
            }
        }

        println("--- STEP TRACKER SERVICE --- started")
    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_REMOVE)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SmartStep is not running in background")
            .setContentText("You can enable this in the app by allowing background access")
            .setSmallIcon(R.drawable.splash_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(false)

        if(notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(1, notification.build())
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