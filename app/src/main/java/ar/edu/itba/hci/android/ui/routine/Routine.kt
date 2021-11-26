package ar.edu.itba.hci.android.ui.routine

import ar.edu.itba.hci.android.api.model.Routine
import ar.edu.itba.hci.android.api.model.User
import java.util.*

data class Routine(
    val id:Int,
    val name:String,
    val description:String,
    val durationMinutes:Int,
    val difficulty:String,
    val cycles:List<Cycle>,
    val date: Date?,
    val user: User,
    val isPublic: Boolean,
    val metadata: Routine.Metadata
) {
    val shareLink = "http://traningapp.com/routine?id=${id}"
}
