package ar.edu.itba.hci.android.api.service

import ar.edu.itba.hci.android.api.ApiResponse
import ar.edu.itba.hci.android.api.model.Cycle
import ar.edu.itba.hci.android.api.model.CycleExercise
import ar.edu.itba.hci.android.api.model.PagedList
import ar.edu.itba.hci.android.api.model.Routine
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoutineService {
    @GET("routines/{routineID}")
    suspend fun getRoutine(@Path("routineID") routineID:Int) : ApiResponse<Routine>

    @GET("routines/{routineID}/cycles")
    suspend fun getRoutineCycles(@Path("routineID") routineID: Int) : ApiResponse<PagedList<Cycle>>

    @GET("cycles/{cycleID}/exercises")
    suspend fun getCycleExercises(@Path("cycleID") cycleID:Int) : ApiResponse<PagedList<CycleExercise>>

    @PUT("routines/{routineID}")
    suspend fun modifyRoutine(@Path("routineID") routineID: Int, @Body routine:Routine)
}