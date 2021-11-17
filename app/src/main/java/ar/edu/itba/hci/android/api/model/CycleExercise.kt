package ar.edu.itba.hci.android.api.model

data class CycleExercise(
    val order:Int,
    val duration:Int,
    val repetitions:Int,
    val exercise:Exercise
)