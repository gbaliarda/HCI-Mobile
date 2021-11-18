package ar.edu.itba.hci.android.ui.routine

import android.util.Log
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
import kotlin.Exception

class RoutineViewModel(private val routineID: Int, private val app: MainApplication) : ViewModel() {
    val routine: LiveData<Routine> by lazy {
        MutableLiveData<Routine>().also {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val routine = app.routineRepository.getRoutine(routineID)
                    it.postValue(routine)
                    routine.metadata.score?.let { score ->
                        scoreValue.postValue(score)
                    }
                    routine.metadata.favorite?.let { fav ->
                        liked.postValue(fav)
                    }
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

    val scoreValue: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            it.value = 0
        }
    }

    fun likeRoutine() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(routine.value?.metadata == null)
                    throw Exception()
                routine.value?.metadata?.favorite = routine.value?.metadata?.favorite == null || routine.value?.metadata?.favorite == false
                app.routineRepository.modifyRoutine(routineID, routine.value!!)
            } catch(ex:Exception){
                Log.d("likeRoutine", "error")
            }
        }
    }

    fun scoreRoutine(newScore:Int) {
        Log.d("newscore",newScore.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                routine.value?.metadata?.score = newScore
                app.routineRepository.modifyRoutine(routineID, routine.value!!)
            } catch(ex:Exception) {
                Log.d("scoreRoutine", "error")
            }
        }
    }
    }
