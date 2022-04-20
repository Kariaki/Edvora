package com.test.edvora.api

import com.test.edvora.model.Ride
import com.test.edvora.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface EdvoraApi {

    @GET("rides")
    suspend fun getRides(): Response<List<Ride>>

    @GET("user")
    suspend fun getUser(): Response<User>
}