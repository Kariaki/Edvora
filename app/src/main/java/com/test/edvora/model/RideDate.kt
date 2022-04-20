package com.test.edvora.model

import java.util.*


class RideDate(val date: String) {


    var split: List<String> = date.split("/")

    fun before(inputDate: String): Boolean {
        val inputSplit = inputDate.split("/")
        val inputDate = createDate(
            day = inputSplit[1].toInt(),
            month = inputSplit[0].toInt(),
            year = inputSplit[2].toInt()
        )
        val currentDate = createDate(
            day = split[1].toInt(),
            month = split[0].toInt(),
            year = split[2].toInt()
        )


        return currentDate.before(inputDate)
    }

    fun after(inputDate: String): Boolean {
        val inputSplit = inputDate.split("/")
        val inputDate = createDate(
            day = inputSplit[1].toInt(),
            month = inputSplit[0].toInt(),
            year = inputSplit[2].toInt()
        )
        val currentDate = createDate(
            day = split[1].toInt(),
            month = split[0].toInt(),
            year = split[2].toInt()
        )


        return currentDate.after(inputDate)
    }

    private fun createDate(day: Int, month: Int, year: Int): Date {
        return Date(year, month, day)
    }

}