package com.githubissuetracker.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun formatDateString(
        originalFormat: String, outputFormat: String, dateString: String
    ): String {
        val sdf = SimpleDateFormat(originalFormat)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        try {
            calendar.time = sdf.parse(dateString)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val returnFormat = SimpleDateFormat(outputFormat)
        return returnFormat.format(calendar.time)
    }
}