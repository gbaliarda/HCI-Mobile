package ar.edu.itba.hci.android.api

import ar.edu.itba.hci.android.api.model.ApiError
import java.io.IOException

typealias ApiResponse<T> = NetworkResponse<T,ApiError>

sealed class NetworkResponse<out T, out U> {
    data class Success<T : Any>(val body:T) : NetworkResponse<T, Nothing>()

    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    data class NetworkError(val error: IOException) : NetworkResponse<Nothing,Nothing>()

    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing,Nothing>()
}
