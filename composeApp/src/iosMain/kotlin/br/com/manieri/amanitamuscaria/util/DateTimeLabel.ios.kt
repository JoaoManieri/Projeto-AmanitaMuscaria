package br.com.manieri.amanitamuscaria.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSLocale.Companion.localeWithLocaleIdentifier

actual fun currentDateTimeLabel(): String {
    val formatter = NSDateFormatter()
    formatter.locale = localeWithLocaleIdentifier("pt_BR")
    formatter.dateFormat = "dd/MM/yyyy HH:mm:ss"
    return formatter.stringFromDate(NSDate())
}
