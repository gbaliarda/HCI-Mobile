package ar.edu.itba.hci.android.api.model

import java.util.*

data class Routine(
    var id:Int,
    val name:String,
    val detail:String,
    val date: Date?,
    val difficulty:String,
    val category: Category?,
    val user: User,
    val isPublic: Boolean,
    val metadata:Metadata,
) {
    data class Metadata(
        val duration:Int,
        var favorite:Boolean?,
        var score:Int?
    )
}
