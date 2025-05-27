package io.github.kei_1111.withmo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.domain.model.user_settings.ThemeType
import io.github.kei_1111.withmo.utils.TimeUtils.TimePeriods.EveningStartTime
import io.github.kei_1111.withmo.utils.TimeUtils.TimePeriods.MorningStartTime
import io.github.kei_1111.withmo.utils.TimeUtils.TimePeriods.NightStartTime
import java.time.LocalTime
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data object TimeUtils {
    @Suppress("MagicNumber")
    data object TimePeriods {
        val MorningStartTime: LocalTime = LocalTime.of(6, 0)
        val EveningStartTime: LocalTime = LocalTime.of(17, 0)
        val NightStartTime: LocalTime = LocalTime.of(19, 0)
    }

    private var hasSentMorningMessage = false
    private var hasSentEveningMessage = false
    private var hasSentNightMessage = false

    private fun sendMorningMessage() {
        AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.ChangeDay, "")
        hasSentMorningMessage = true
    }

    private fun sendEveningMessage() {
        AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.ChangeEvening, "")
        hasSentEveningMessage = true
    }

    private fun sendNightMessage() {
        AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.ChangeNight, "")
        hasSentNightMessage = true
    }

    fun isMorning(currentTime: LocalTime): Boolean {
        return currentTime.isAfter(MorningStartTime) && currentTime.isBefore(EveningStartTime)
    }

    fun isEvening(currentTime: LocalTime): Boolean {
        return currentTime.isAfter(EveningStartTime) && currentTime.isBefore(NightStartTime)
    }

    fun isNight(currentTime: LocalTime): Boolean {
        return currentTime.isAfter(NightStartTime) || currentTime.isBefore(MorningStartTime)
    }

    fun sendTimeBasedMessage(currentZonedDateTime: ZonedDateTime) {
        val currentTime = currentZonedDateTime.toLocalTime()

        when {
            isMorning(currentTime) && !hasSentMorningMessage -> sendMorningMessage()
            isEvening(currentTime) && !hasSentEveningMessage -> sendEveningMessage()
            isNight(currentTime) && !hasSentNightMessage -> sendNightMessage()
        }
    }

    fun resetFlags() {
        hasSentMorningMessage = false
        hasSentEveningMessage = false
        hasSentNightMessage = false
    }

    fun themeMessage(themeType: ThemeType) {
        val currentTime = LocalTime.now()
        when (themeType) {
            ThemeType.TIME_BASED -> {
                when {
                    isMorning(currentTime) -> {
                        AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.ChangeDay, "")
                    }
                    isEvening(currentTime) -> {
                        AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.ChangeEvening, "")
                    }
                    isNight(currentTime) -> {
                        AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.ChangeNight, "")
                    }
                }
            }
            ThemeType.LIGHT -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.SetDayFixedMode, "")
            }
            ThemeType.DARK -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SkyBlend, UnityMethod.SetNightFixedMode, "")
            }
        }
    }
}
