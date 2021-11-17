package ar.edu.itba.hci.android.ui.routine

data class Cycle(
    val id:Int,
    val name:String,
    val repetitions:Int,
    val exercises: List<Exercise>
)
