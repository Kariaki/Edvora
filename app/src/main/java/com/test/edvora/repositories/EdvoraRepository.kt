package com.test.edvora.repositories

import com.test.edvora.api.EdvoraApi
import com.test.edvora.dao.EdvoraDao
import com.test.edvora.model.Ride
import com.test.edvora.model.RideDate
import com.test.edvora.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.await
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject


class EdvoraRepository @Inject constructor(private val api: EdvoraApi) : EdvoraDao {


    override suspend fun getUsers(): User? = api.getUser().body()

    override suspend fun getRides(): Response<List<Ride>> = api.getRides()

    override suspend fun getTestRides(): List<Ride> {
        TODO("Not yet implemented")
    }


    override suspend fun getUpcomingRides(): List<Ride> = withContext(Dispatchers.IO) {

        if (getRides().isSuccessful) getRides().body()?.filter {

            val currentDate = getDate(System.currentTimeMillis())
            val rideDate = RideDate(it.date.split(" ")[0])
            rideDate.after(currentDate)
        }!! else listOf()

    }

    override suspend fun getPastRides(): List<Ride> = withContext(Dispatchers.IO) {

        if (getRides().isSuccessful) getRides().body()?.filter {

            val currentDate = getDate(System.currentTimeMillis())
            val rideDate = RideDate(it.date.split(" ")[0])
            rideDate.before(currentDate)
        }!! else listOf()

    }

    override suspend fun getNearestRides(stationCode: Int): List<Ride> = withContext(Dispatchers.IO) {

        if (getRides().isSuccessful) getRides().body()

            ?.sortedBy { it.getDistance(stationCode)
                 }!!
        else listOf()
    }

    override suspend fun getCities(): List<String> = withContext(Dispatchers.IO) {
        if (getRides().isSuccessful) {
            val result = getRides().body()?.distinctBy { it.city }?.map { it.city }
            result!!
        } else {
            listOf()
        }
    }


    override suspend fun getRideByCity(city: String): List<Ride> = withContext(Dispatchers.IO) {

        if (getRides().isSuccessful) getRides().body()?.filter {

            it.city == city
        }!! else listOf()

    }

    fun getDate(timeStamp: Long): String {
        val stamp = Timestamp(timeStamp)
        val date = Date(stamp.time)
        val f: DateFormat = SimpleDateFormat("MM/dd/yyyy")

        return f.format(date)
    }

    override suspend fun getCityStates(city: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getRidesByStates(): List<Ride> {
        TODO("Not yet implemented")
    }

}