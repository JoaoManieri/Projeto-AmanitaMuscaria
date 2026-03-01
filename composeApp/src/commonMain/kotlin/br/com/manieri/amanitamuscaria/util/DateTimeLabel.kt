package br.com.manieri.amanitamuscaria.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentDateTimeLabel(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val day = now.dayOfMonth.toString().padStart(2, '0')
    val month = now.monthNumber.toString().padStart(2, '0')
    val year = now.year.toString()
    val hour = now.hour.toString().padStart(2, '0')
    val minute = now.minute.toString().padStart(2, '0')
    val second = now.second.toString().padStart(2, '0')
    return "$day/$month/$year $hour:$minute:$second"
}
