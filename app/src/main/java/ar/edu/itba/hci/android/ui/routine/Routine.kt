package ar.edu.itba.hci.android.ui.routine

data class Routine(
    val name:String,
    val description:String,
    val durationMinutes:Int,
    val difficulty:String,
    val cycles:List<Cycle>
) {
    val shareLink = "http://traningapp.com/routine?id=1"
}
