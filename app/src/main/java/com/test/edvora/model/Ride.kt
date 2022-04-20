package com.test.edvora.model

import com.kcoding.recyclerview_helper.SuperEntity
import kotlin.math.min

data class Ride(
    val id: Int,
    val origin_station_code: Int,
    val station_path: IntArray,
    val destination_station_code: Int,
    val date: String,
    val map_url: String,
    val state: String,
    val city: String,
    var distance:Int?=null
) : SuperEntity() {


    fun getDistance(stationCode: Int): Int {

        val pathArray = station_path
        var output = 0
        for (i in pathArray.indices) {
            val it = pathArray[i]
            if (it == stationCode) break

            if (it > stationCode) {
                output = if (i != 0) {
                    val current = it - stationCode

                    val previous = stationCode % pathArray[i - 1]
                    val min = min(current, previous)
                    min

                } else {
                    it - stationCode
                }
                break
            }
        }
        return output
    }


}