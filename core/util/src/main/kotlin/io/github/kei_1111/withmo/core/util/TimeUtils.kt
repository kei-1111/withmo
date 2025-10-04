package io.github.kei_1111.withmo.core.util

import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.TimeUtils.TimePeriods.EveningStartTime
import io.github.kei_1111.withmo.core.util.TimeUtils.TimePeriods.MorningStartTime
import io.github.kei_1111.withmo.core.util.TimeUtils.TimePeriods.NightStartTime
import java.time.LocalTime
import java.time.ZonedDateTime

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
        AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.CHANGE_DAY, "")
        hasSentMorningMessage = true
    }

    private fun sendEveningMessage() {
        AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.CHANGE_EVENING, "")
        hasSentEveningMessage = true
    }

    private fun sendNightMessage() {
        AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.CHANGE_NIGHT, "")
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
                        AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.CHANGE_DAY, "")
                    }
                    isEvening(currentTime) -> {
                        AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.CHANGE_EVENING, "")
                    }
                    isNight(currentTime) -> {
                        AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.CHANGE_NIGHT, "")
                    }
                }
            }
            ThemeType.LIGHT -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.SET_DAY_FIXED_MODE, "")
            }
            ThemeType.DARK -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SKY_BLEND, UnityMethod.SET_NIGHT_FIXED_MODE, "")
            }
        }
    }
}
