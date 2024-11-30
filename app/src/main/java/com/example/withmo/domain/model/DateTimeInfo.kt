package com.example.withmo.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.O)
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
