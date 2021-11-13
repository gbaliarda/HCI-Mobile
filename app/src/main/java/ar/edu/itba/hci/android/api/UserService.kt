package ar.edu.itba.hci.android.api

import ar.edu.itba.hci.android.api.model.*
import retrofit2.http.*

interface UserService {
    @POST("users/login")
    suspend fun login(@Body credentials: Credentials) : ApiResponse<Token>

    @POST("users/logout")
    suspend fun logout()

    @GET("users/current")
    suspend fun getCurrentUser() : ApiResponse<User>

    @GET("users/current/routines")
    suspend fun getCurrentUserRoutines() : ApiResponse<PagedList<Routine>>

    @GET("users/{userID}/routines")
    suspend fun getUserRoutines(@Path("userID") userID: Int) : ApiResponse<PagedList<Routine>>

    @GET("users/current/executions")
    suspend fun getCurrentUserExecutions() : ApiResponse<PagedList<Execution>>

    @GET("users/current/reviews")
    suspend fun getCurrentUserReviews() : ApiResponse<PagedList<Review>>
}
