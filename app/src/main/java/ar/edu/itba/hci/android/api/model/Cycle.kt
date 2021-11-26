package ar.edu.itba.hci.android.api.model

data class Cycle(
    val id:Int,
    val name:String,
    val detail:String,
    val type:String,
    val order:Int,
    val repetitions:Int,
    val metadata:Metadata
) {
    data class Metadata(
        val descansos:List<Int>
    )
}
