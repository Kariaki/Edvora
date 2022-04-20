package com.test.edvora.dao

import com.test.edvora.model.Ride
import com.test.edvora.model.User
import retrofit2.Response

interface EdvoraDao {

    suspend fun getUpcomingRides(): List<Ride>

    suspend fun getPastRides(): List<Ride>
    suspend fun getUsers():User?
    suspend fun getNearestRides(stationCode: Int): List<Ride>
    suspend fun getRides(): Response<List<Ride>>
    suspend fun getTestRides(): List<Ride>
    suspend fun getCities(): List<String>
    suspend fun getCityStates(city: String):List<String>
    suspend fun getRidesByStates():List<Ride>
    suspend fun getRideByCity(city: String): List<Ride>

}