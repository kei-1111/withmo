package io.github.kei_1111.withmo.core.model

import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

data class DateTimeInfo(
    val year: String = "2024",
    val month: String = "01",
    val day: String = "01",
    val dayOfWeek: String = "MON",
    val hour: String = "00",
    val minute: String = "00",
)

fun ZonedDateTime.toDateTimeInfo(): DateTimeInfo {
    return DateTimeInfo(
        year = year.toString(),
        month = String.format(Locale.JAPAN, "%02d", monthValue),
        day = String.format(Locale.JAPAN, "%02d", dayOfMonth),
        hour = String.format(Locale.JAPAN, "%02d", hour),
        minute = String.format(Locale.JAPAN, "%02d", minute),
        dayOfWeek = dayOfWeek.getDisplayName(
            TextStyle.SHORT,
            Locale.ENGLISH,
        ).uppercase(),
    )
}
