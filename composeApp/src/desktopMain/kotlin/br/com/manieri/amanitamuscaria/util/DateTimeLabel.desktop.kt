package br.com.manieri.amanitamuscaria.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

actual fun currentDateTimeLabel(): String {
    return LocalDateTime.now().format(dateTimeFormatter)
}
