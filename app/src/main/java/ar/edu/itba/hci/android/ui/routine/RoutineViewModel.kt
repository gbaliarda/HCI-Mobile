package ar.edu.itba.hci.android.ui.routine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutineViewModel : ViewModel() {
    val routine: LiveData<Routine> by lazy {
        MutableLiveData<Routine>().also {
            it.value = Routine("Piernas", 7, "Intermedia", listOf(
                Exercise("Sentadillas", Exercise.RepetitionType.TIMES, 5, 3),
                Exercise("Soga", Exercise.RepetitionType.SECONDS, 20, 4),
                Exercise("Estocadas", Exercise.RepetitionType.TIMES, 5, 3),
            ))
        }
    }

    val liked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = false
        }
    }
}