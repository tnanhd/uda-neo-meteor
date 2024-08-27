package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getCurrentDateString(): String = SimpleDateFormat(
    Constants.API_QUERY_DATE_FORMAT,
    Locale.getDefault()
).format(Calendar.getInstance().time)

fun getCurrentDateStringPlusDays(days: Int): String = SimpleDateFormat(
    Constants.API_QUERY_DATE_FORMAT,
    Locale.getDefault()
).format(Calendar.getInstance().apply {
    add(Calendar.DAY_OF_YEAR, days)
}.time)