package com.test.edvora.repositories

import com.test.edvora.dao.EdvoraDao
import com.test.edvora.model.Ride
import com.test.edvora.model.RideDate
import com.test.edvora.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat

class FakeEdvoraRepository : EdvoraDao {
    override suspend fun getUpcomingRides(): List<Ride> = withContext(Dispatchers.IO) {

        allRides.filter {

            val currentDate = getDate(System.currentTimeMillis())
            val rideDate = RideDate(it.date.split(" ")[0])
            rideDate.after(currentDate)
        }

    }
    override suspend fun getPastRides(): List<Ride> = withContext(Dispatchers.IO) {

        allRides.filter {

            val currentDate = getDate(System.currentTimeMillis())
            val rideDate = RideDate(it.date.split(" ")[0])
            rideDate.before(currentDate)
        }

    }

    val user = User(station_code = 40, name = "Dhruv Singh", url = "https://picsum.photos/200")
    val allRides: List<Ride> = listOf(
        Ride(
            707,
            13,
            intArrayOf(40, 57, 64, 78, 83),
            96,
            "03/08/2022 06:52 PM",
            "https://picsum.photos/200",
            "Meghalaya",
            "Tura"
        ) , Ride(
            754,
            10,
            intArrayOf(31, 41, 52, 65, 73,81),
            95,
            "02/10/2022 08:11 PM",
            "https://picsum.photos/200",
            "Uttar Predesh",
            "Hardoi"
        ), Ride(
            905,
            5,
            intArrayOf(49,51,67,78,84),
            93,
            "03/19/2022 10:42 PM",
            "https://picsum.photos/200",
            "Madhya Predesh",
            "Manawar"
        ), Ride(
            603,
            2,
            intArrayOf(47,51,60,72,84),
            96,
            "03/01/2022 10:42 PM",
            "https://picsum.photos/200",
            "Maharashtra",
            "Partur"
        )
    )

    override suspend fun getUsers(): User {

        return user
    }

    override suspend fun getNearestRides(stationCode: Int): List<Ride> = withContext(Dispatchers.IO) {
        allRides.sortedBy { it.getDistance(stationCode) }

    }

    override suspend fun getRides(): Response<List<Ride>> = null!!

    override suspend fun getTestRides(): List<Ride> {
        return allRides
    }


    override suspend fun getCities(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getCityStates(city: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getRidesByStates(): List<Ride> {
        TODO("Not yet implemented")
    }

    override suspend fun getRideByCity(city: String): List<Ride> {
        TODO("Not yet implemented")
    }

    fun getDate(timeStamp: Long): String {
        val stamp = Timestamp(timeStamp)
        val date = Date(stamp.time)
        val f: DateFormat = SimpleDateFormat("MM/dd/yyyy")

        return f.format(date)
    }
}