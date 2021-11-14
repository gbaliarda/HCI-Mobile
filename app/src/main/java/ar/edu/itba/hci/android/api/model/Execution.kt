package ar.edu.itba.hci.android.api.model

import java.util.*

data class Execution(
    val id:Int,
    val date:Date,
    val duration:Int,
    val wasModified:Boolean,
    val routine:Routine,
)
