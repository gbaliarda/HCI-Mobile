package ar.edu.itba.hci.android.api.model

data class ApiError (
    val code:Int,
    val description:String?,
    val details:List<String>?,
) : Exception()
