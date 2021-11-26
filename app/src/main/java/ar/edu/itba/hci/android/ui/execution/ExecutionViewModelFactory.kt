package ar.edu.itba.hci.android.ui.execution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.ui.routine.RoutineViewModel

class ExecutionViewModelFactory(private val app: MainApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExecutionViewModel::class.java)) {
            return ExecutionViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
