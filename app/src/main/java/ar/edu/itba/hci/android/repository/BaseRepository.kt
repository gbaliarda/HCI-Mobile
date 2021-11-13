package ar.edu.itba.hci.android.repository

import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.api.Api
import ar.edu.itba.hci.android.api.ApiResponse
import ar.edu.itba.hci.android.api.NetworkResponse
import ar.edu.itba.hci.android.api.UserService

abstract class BaseRepository<T>(application: MainApplication, service:Class<T>) {
    protected val apiService:T = Api.createService(application, service)

    protected fun <T> unwrapResponse(response: ApiResponse<T>) : T {
        if(response is NetworkResponse.Success) return response.body

        if(response is NetworkResponse.ApiError) throw response.body
        if(response is NetworkResponse.NetworkError) throw Exception("API Network Exception")

        throw Exception("API Unknown Exception")
    }
}