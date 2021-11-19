package ar.edu.itba.hci.android.ui.execution

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import ar.edu.itba.hci.android.MainActivity
import ar.edu.itba.hci.android.R
import kotlinx.coroutines.*
import java.lang.IllegalStateException

class ExecutionService : LifecycleService() {
    companion object {
        private const val NOTIF_CHANNEL_ID = "execution_channel"
        private const val NOTIF_ID = 1
        private const val ACTION_PAUSE = "PAUSE"
        private const val ACTION_RESUME = "RESUME"
    }

    var model: ExecutionViewModel? = null

    private val binder = Binder()
    private var job: Job? = null
    private val broadcastReceiver = ExecutionActionReceiver()

    private val openPendingIntent
        get() = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    private suspend fun backgroundThread() {
        var timer = model!!.timer.value!!
        model!!.timer.postValue(timer)

        while (timer > 0) {
            delay(1000)
            timer--
            model!!.timer.postValue(timer)
        }
        stopForeground(false)
    }

    fun pause() {
        if (model?.timerRunning?.value != true) return

        stopForeground(false)
        model!!.timerRunning.postValue(false)
        job?.cancel()
    }

    fun resume() {
        if(model?.timerRunning?.value != false) return

        job?.cancel()
        startForeground(NOTIF_ID, buildNotification())
        model!!.timerRunning.postValue(true)
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

        val filter = IntentFilter()
        filter.addAction(ACTION_PAUSE)
        filter.addAction(ACTION_RESUME)
        registerReceiver(broadcastReceiver, filter)

        createNotificationChannel()

        model!!.timer.observe(this, {
            updateNotif()
        })

        model!!.timerRunning.observe(this, {
            updateNotif()
        })

        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun updateNotif() {
        val manager =
            getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIF_ID, buildNotification())
    }

    private fun buildNotification(): Notification {
        val running = model!!.timerRunning.value!!

        val buttonIntent = Intent(
            when (running) {
                true -> ACTION_PAUSE
                else -> ACTION_RESUME
            }
        )
        val pendingButtonIntent =
            PendingIntent.getBroadcast(this, 0, buttonIntent, PendingIntent.FLAG_IMMUTABLE)

        val buttonAction = when (running) {
            true -> NotificationCompat.Action(
                R.drawable.ic_baseline_pause_24,
                getString(R.string.pause),
                pendingButtonIntent
            )
            else -> NotificationCompat.Action(
                R.drawable.ic_baseline_play_arrow_24,
                getString(R.string.resume),
                pendingButtonIntent
            )
        }

        return NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
            .setContentTitle(getString(R.string.execution_notif_title, model!!.routine.value!!.name))
            .setContentText(getString(R.string.execution_notif_content, model!!.timer.value!!))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(openPendingIntent)
            .addAction(buttonAction)
            .build()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.execution_channel_title)
        val desc = getString(R.string.execution_channel_desc)
        val importance = NotificationManager.IMPORTANCE_LOW
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

            when (intent.action) {
                ACTION_PAUSE -> {
                    pause()
                }
                ACTION_RESUME -> {
                    resume()
                }
                else -> throw IllegalStateException()
            }
        }
    }
}