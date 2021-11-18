package ar.edu.itba.hci.android.ui.routine

data class Exercise(
    val name:String,
    val repetitionType: RepetitionType,
    val repetitionValue: Int,
    val description: String,
    val group: String,
    val difficulty: String
) {
    enum class RepetitionType {
        TIMES,
        SECONDS,
        MINUTES
    }
}
