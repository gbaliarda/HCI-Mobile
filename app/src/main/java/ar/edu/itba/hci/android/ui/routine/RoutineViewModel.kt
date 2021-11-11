package ar.edu.itba.hci.android.ui.routine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutineViewModel : ViewModel() {
    val exercises: MutableLiveData<List<Exercise>> by lazy {
        MutableLiveData<List<Exercise>>().also {
            it.value = listOf(
                Exercise("Sentadillas", 5, null, 3),
                Exercise("Soga", null, 20, 4),
                Exercise("Estocadas", 5, null, 3),
            )
        }
    }
}