package ar.edu.itba.hci.android.api.model

import java.util.*

data class Routine(
    val id:Int,
    val name:String,
    val detail:String,
    val date: Date?,
    val score:Int?,
    val difficulty:String,
    val category: Category?,
    val user: User,
    val metadata:Metadata?
) {
    data class Metadata(
        val favorite:Boolean?
    )
}
