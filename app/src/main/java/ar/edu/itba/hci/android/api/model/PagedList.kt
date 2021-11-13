package ar.edu.itba.hci.android.api.model

data class PagedList<T>(
    val totalCount:Int,
    val orderBy:String,
    val direction:String,
    val content:List<T>,
    val size:Int,
    val page:Int,
    val isLastPage:Boolean
)
