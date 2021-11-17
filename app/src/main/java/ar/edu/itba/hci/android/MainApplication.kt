package ar.edu.itba.hci.android

import android.app.Application
import ar.edu.itba.hci.android.repository.RoutineRepository
import ar.edu.itba.hci.android.repository.UserRepository

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
    }
}
