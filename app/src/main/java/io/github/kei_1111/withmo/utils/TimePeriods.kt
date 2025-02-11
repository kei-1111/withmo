package io.github.kei_1111.withmo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.utils.TimePeriods.EveningStartTime
import io.github.kei_1111.withmo.utils.TimePeriods.MorningStartTime
import io.github.kei_1111.withmo.utils.TimePeriods.NightStartTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("MagicNumber")
data object TimePeriods {
    val MorningStartTime: LocalTime = LocalTime.of(6, 0)
    val EveningStartTime: LocalTime = LocalTime.of(17, 0)
    val NightStartTime: LocalTime = LocalTime.of(19, 0)
}

@RequiresApi(Build.VERSION_CODES.O)
fun isMorning(currentTime: LocalTime): Boolean {
    return currentTime.isAfter(MorningStartTime) && currentTime.isBefore(EveningStartTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun isEvening(currentTime: LocalTime): Boolean {
    return currentTime.isAfter(EveningStartTime) && currentTime.isBefore(NightStartTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun isNight(currentTime: LocalTime): Boolean {
    return currentTime.isAfter(NightStartTime) || currentTime.isBefore(MorningStartTime)
}
