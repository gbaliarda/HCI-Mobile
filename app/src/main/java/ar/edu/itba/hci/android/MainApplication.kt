package ar.edu.itba.hci.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.hci.android.repository.RoutineRepository
import ar.edu.itba.hci.android.repository.UserRepository
import ar.edu.itba.hci.android.ui.execution.ExecutionService

class MainApplication : Application() {
    lateinit var preferences:AppPreferences
        private set

    lateinit var userRepository:UserRepository
        private set

    lateinit var routineRepository: RoutineRepository
        private set

    override fun onCreate() {
        super.onCreate()
        preferences = AppPreferences(this)
        userRepository = UserRepository(this)
        routineRepository = RoutineRepository(this)
        createExecutionNotificationChannel()
    }

    private fun createExecutionNotificationChannel() {
        val name = getString(R.string.execution_channel_title)
        val desc = getString(R.string.execution_channel_desc)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(ExecutionService.NOTIF_CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager =
            getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
