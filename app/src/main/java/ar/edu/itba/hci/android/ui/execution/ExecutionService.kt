package ar.edu.itba.hci.android.ui.execution

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import ar.edu.itba.hci.android.MainActivity
import ar.edu.itba.hci.android.R
import kotlinx.coroutines.*
import java.lang.IllegalStateException

class ExecutionService : LifecycleService() {
    companion object {
        private const val NOTIF_CHANNEL_ID = "execution_channel"
    }

    var model: ExecutionViewModel? = null

    private val binder = Binder()
    private var job: Job? = null
    private val broadcastReceiver = ExecutionActionReceiver()

    private suspend fun backgroundThread() {
        var timer = 30
        model!!.timer.postValue(timer)

        while (timer > 0) {
            delay(1000)
            timer--
            model!!.timer.postValue(timer)
        }
        stopForeground(true)
    }

    private fun updateNotif() {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val manager =
            getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, buildNotification(pendingIntent))
    }

    fun pause() {
        stopForeground(Service.STOP_FOREGROUND_DETACH)
        model!!.running.postValue(false)
        job?.cancel()
    }

    fun resume() {
        model!!.running.postValue(true)
        job = lifecycleScope.launch {
            backgroundThread()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val filter = IntentFilter()
        filter.addAction("PAUSE")
        filter.addAction("RESUME")
        registerReceiver(broadcastReceiver, filter)

        createNotificationChannel()
        startForeground(1, buildNotification(pendingIntent))

        model!!.timer.observe(this, {
            updateNotif()
        })

        model!!.running.observe(this, {
            updateNotif()
        })

        // If we get killed, after returning from here, restart
        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun buildNotification(intent: PendingIntent): Notification {
        val running = model!!.running.value!!

        val buttonIntent = Intent(when(running) {
            true -> "PAUSE"
            else -> "RESUME"
        })
        val pendingButtonIntent =
            PendingIntent.getBroadcast(this, 0, buttonIntent, PendingIntent.FLAG_IMMUTABLE)

        val buttonAction = when(running) {
            true -> NotificationCompat.Action(
                R.drawable.ic_baseline_pause_24,
                "Pause",
                pendingButtonIntent
            )
            else -> NotificationCompat.Action(
                R.drawable.ic_baseline_play_arrow_24,
                "Play",
                pendingButtonIntent
            )
        }

        return NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
            .setContentTitle("Ejecución ${model!!.routine.value!!.name}")
            .setContentText("Timer: ${model!!.timer.value!!}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(intent)
            .addAction(buttonAction)
            .build()
    }

    private fun createNotificationChannel() {
        val name = "Executions"
        val desc = "Execution notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIF_CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager =
            getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    inner class Binder : android.os.Binder() {
        val service: ExecutionService get() = this@ExecutionService
    }

    inner class ExecutionActionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return

            when(intent.action) {
                "PAUSE" -> {
                    pause()
                }
                "RESUME" -> {
                    resume()
                }
                else -> throw IllegalStateException()
            }
        }
    }
}