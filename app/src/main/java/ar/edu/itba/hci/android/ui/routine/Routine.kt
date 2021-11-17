package ar.edu.itba.hci.android.ui.routine

data class Routine(
    val id:Int,
    val name:String,
    val description:String,
    val durationMinutes:Int,
    val difficulty:String,
    val cycles:List<Cycle>
) {
    val shareLink = "https://traning.com/rutina/${id}"
}
