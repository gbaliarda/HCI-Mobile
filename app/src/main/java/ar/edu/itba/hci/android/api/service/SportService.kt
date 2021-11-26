package ar.edu.itba.hci.android.api.service

import ar.edu.itba.hci.android.api.ApiResponse
import ar.edu.itba.hci.android.api.model.PagedList
import ar.edu.itba.hci.android.api.model.Sport
import retrofit2.http.*

interface SportService {
    @GET("sports")
    suspend fun getSports(): ApiResponse<PagedList<Sport>>

    @POST("sports")
    suspend fun addSport(@Body sport:Sport): ApiResponse<Sport>

    @GET("sports/{sportID}")
    suspend fun getSport(@Path("sportID") sportID:Int): ApiResponse<Sport>

    @PUT("sports/{sportID}")
    suspend fun modifySport(@Path("sportID") sportID: Int, @Body sport:Sport): ApiResponse<Sport>

    @DELETE("sports/{sportID}")
    suspend fun deleteSport(@Path("sportID") sportID:Int)
}