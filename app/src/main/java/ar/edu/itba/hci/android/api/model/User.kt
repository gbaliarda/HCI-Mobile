package ar.edu.itba.hci.android.api.model

import java.util.*

data class User(
    val id:Int,
    val username:String,
    val firstName:String,
    val lastName:String?,
    val avatarUrl:String?,
    val email:String?
)
