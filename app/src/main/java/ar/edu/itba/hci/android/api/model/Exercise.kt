package ar.edu.itba.hci.android.api.model

import java.util.*

data class Exercise(
    val id:Int,
    val name:String,
    val detail:String,
    val type:String,
    val duration:Int,
    val date:Date,
    val metadata: Metadata
) {
    data class Metadata(
        val dif:String,
        val grupo:String
    )
}
