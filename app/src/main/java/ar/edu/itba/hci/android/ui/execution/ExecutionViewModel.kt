package ar.edu.itba.hci.android.ui.execution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.ui.routine.Cycle
import ar.edu.itba.hci.android.ui.routine.Exercise
import ar.edu.itba.hci.android.ui.routine.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExecutionViewModel(private val app: MainApplication) : ViewModel() {
    private val routineRepository = app.routineRepository

    val running:MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = false
    }

    private val _currentExercise:MutableLiveData<Exercise> = MutableLiveData()
    val currentExercise:LiveData<Exercise> = _currentExercise

    private lateinit var executionList:List<Exercise>
    private var currentExerciseIndex:Int = 0

    fun nextExercise() {
        if(currentExerciseIndex == executionList.size - 1) {
            //TODO:End execution
            return
        }
        currentExerciseIndex++
        _currentExercise.value = executionList[currentExerciseIndex]
        println("Exercise: $currentExerciseIndex")
    }

    fun prevExercise() {
        if(currentExerciseIndex == 0) return

        currentExerciseIndex--
        _currentExercise.value = executionList[currentExerciseIndex]

        println("Exercise: $currentExerciseIndex")
    }

    private fun fillExecutionList(routine:Routine) {
        val list = mutableListOf<Exercise>()
        for(cycle in routine.cycles) {
            for(i in 1..cycle.repetitions) {
                for(exercise in cycle.exercises) {
                    list.add(exercise)
                }
            }
        }
        executionList = list
        _currentExercise.postValue(executionList[currentExerciseIndex])
    }

    var routineID = -1
        set(value) {
            field = value
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val routine = routineRepository.getRoutine(routineID)
                    _routine.postValue(routine)
                    fillExecutionList(routine)
                } catch (ex:Exception) {
                    println(ex)
                }
            }
        }

    private val _routine:MutableLiveData<Routine> = MutableLiveData()
    val routine:LiveData<Routine> = _routine

    val timer: MutableLiveData<Int> = MutableLiveData<Int>().also {
        it.value = 30
    }
}