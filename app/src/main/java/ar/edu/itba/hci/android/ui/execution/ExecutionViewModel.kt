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

class ExecutionViewModel(app: MainApplication) : ViewModel() {
    private val routineRepository = app.routineRepository

    var routineID = -1
        set(value) {
            field = value
            loading.value = true

            if(value == -1) return

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val routine = routineRepository.getRoutine(routineID)
                    _routine.postValue(routine)
                    loading.postValue(false)
                    fillExecutionList(routine)
                } catch (ex:Exception) {
                    println(ex)
                }
            }
        }

    private val _routine:MutableLiveData<Routine> = MutableLiveData()
    val routine:LiveData<Routine> = _routine

    val loading:MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = true
    }

    val timer: MutableLiveData<Int> = MutableLiveData<Int>().also {
        it.value = 30
    }

    val timerRunning:MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = false
    }

    val isPlaying:MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val _finished:MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val finished:LiveData<Boolean> = _finished

    private val _currentExercise:MutableLiveData<ExecutionExercise> = MutableLiveData()
    val currentExercise:LiveData<ExecutionExercise> = _currentExercise

    private val _isFirstExercise:MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = true
    }
    val isFirstExercise:LiveData<Boolean> = _isFirstExercise

    lateinit var executionList:List<ExecutionExercise>
        private set

    var currentExerciseIndex:Int = 0
        private set

    fun nextExercise() {
        if(finished.value == true) {
            return
        }
        if(currentExerciseIndex == executionList.size - 1) {
            _finished.value = true
            timerRunning.value = false
            return
        }

        currentExerciseIndex++

        val currEx = executionList[currentExerciseIndex]
        _currentExercise.value = currEx

        if(currentExerciseIndex > 0)
            _isFirstExercise.value = false

        println("Exercise: $currentExerciseIndex")
    }

    fun prevExercise() {
        if(finished.value == true || currentExerciseIndex == 0) return

        currentExerciseIndex--

        val currEx = executionList[currentExerciseIndex]
        _currentExercise.value = currEx

        if(currentExerciseIndex == 0)
            _isFirstExercise.value = true

        println("Exercise: $currentExerciseIndex")
    }

    fun reset() {
        timerRunning.value = false
        _finished.value = false
        currentExerciseIndex = 0
    }

    private fun fillExecutionList(routine:Routine) {
        val list = mutableListOf<ExecutionExercise>()
        for(cycle in routine.cycles) {
            for(i in 1..cycle.repetitions) {
                for(exercise in cycle.exercises) {
                    list.add(ExecutionExercise(
                        exercise,
                        cycle,
                        i
                    ))
                }
            }
        }
        executionList = list
        _currentExercise.postValue(executionList[currentExerciseIndex])
    }

    data class ExecutionExercise(
        val exercise:Exercise,
        val cycle:Cycle,
        val cycleRepetition:Int,
    )
}