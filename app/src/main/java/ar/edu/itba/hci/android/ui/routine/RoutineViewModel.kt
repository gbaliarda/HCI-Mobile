package ar.edu.itba.hci.android.ui.routine

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.api.model.ApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RoutineViewModel(private val routineID: Int, private val app: MainApplication) : ViewModel() {
    val routine: LiveData<Routine> by lazy {
        MutableLiveData<Routine>().also {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    it.postValue(app.routineRepository.getRoutine(routineID))
                }
                catch (err:ApiError) {
                    println(err.description)
                }
                catch (ex:Exception) {
                    println(ex.message)
                }
            }
        }
    }

    val liked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = false
        }
    }
}