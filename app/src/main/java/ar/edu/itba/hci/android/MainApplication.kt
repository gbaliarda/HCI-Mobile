package ar.edu.itba.hci.android

import android.app.Application
import ar.edu.itba.hci.android.repository.UserRepository

class MainApplication : Application() {
    lateinit var preferences:AppPreferences
        private set

    lateinit var userRepository:UserRepository

    override fun onCreate() {
        super.onCreate()
        preferences = AppPreferences(this)
        userRepository = UserRepository(this)
    }
}
