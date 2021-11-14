package ar.edu.itba.hci.android.api

import java.util.*

data class Review(
    val id:Int,
    val date:Date,
    val score:Int,
    val review:String?
)
