package ar.edu.itba.hci.android.repository

import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.api.service.UserService
import ar.edu.itba.hci.android.api.model.Credentials
import ar.edu.itba.hci.android.api.model.PagedList
import ar.edu.itba.hci.android.api.model.Routine
import java.util.*

class UserRepository(application:MainApplication)
    : BaseRepository<UserService>(application, UserService::class.java) {

    suspend fun login(credentials: Credentials) = unwrapResponse(apiService.login(credentials))

    suspend fun logout() = apiService.logout()

    suspend fun getCurrentUser() = unwrapResponse(apiService.getCurrentUser())

    suspend fun getCurrentUserRoutines() = unwrapResponse(apiService.getCurrentUserRoutines())

    suspend fun getUserRoutines(userID:Int) = unwrapResponse(apiService.getUserRoutines(userID))

    suspend fun getCurrentUserExecutions() = unwrapResponse(apiService.getCurrentUserExecutions())

    suspend fun getCurrentUserReviews() = unwrapResponse(apiService.getCurrentUserReviews())
}
