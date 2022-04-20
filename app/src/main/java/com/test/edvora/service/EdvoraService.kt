package com.test.edvora.service

import com.test.edvora.dao.EdvoraDao
import com.test.edvora.model.Ride
import com.test.edvora.model.RideDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class EdvoraService @Inject constructor(@Named("repository")val dao: EdvoraDao) {

    suspend fun getUsers() = dao.getUsers()

    suspend fun getRides(): Response<List<Ride>> = dao.getRides()

    suspend fun getTestRides():List<Ride> = dao.getTestRides()


    suspend fun getUpcomingRides(): List<Ride> = dao.getUpcomingRides()

    suspend fun getPastRides(): List<Ride> = dao.getPastRides()

    suspend fun getNearestRides(stationCode: Int): List<Ride> = dao.getNearestRides(stationCode)

    suspend fun getCities(): List<String> = dao.getCities()

    suspend fun getCityStates(city: String): List<String> = dao.getCityStates(city)

    suspend fun getRidesByStates(): List<Ride> = dao.getRidesByStates()

    suspend fun getRideByCity(city: String): List<Ride> = dao.getRideByCity(city)


}