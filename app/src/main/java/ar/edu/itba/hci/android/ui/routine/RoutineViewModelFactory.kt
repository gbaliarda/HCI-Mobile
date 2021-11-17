package ar.edu.itba.hci.android.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.ui.login.LoginViewModel

class RoutineViewModelFactory(private val routineID: Int, private val app: MainApplication) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            return RoutineViewModel(routineID, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}