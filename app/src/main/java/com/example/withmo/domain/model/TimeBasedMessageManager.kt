package com.example.withmo.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.unity3d.player.UnityPlayer
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class TimeBasedMessageManager {

    private var hasSentMorningMessage = false
    private var hasSentEveningMessage = false
    private var hasSentNightMessage = false

    private fun shouldSendMorningMessage(currentTime: LocalTime): Boolean {
        return currentTime.isAfter(MorningStartTime) && currentTime.isBefore(EveningStartTime) && !hasSentMorningMessage
    }

    private fun shouldSendEveningMessage(currentTime: LocalTime): Boolean {
        return currentTime.isAfter(EveningStartTime) && currentTime.isBefore(NightStartTime) && !hasSentEveningMessage
    }

    private fun shouldSendNightMessage(currentTime: LocalTime): Boolean {
        return (currentTime.isAfter(NightStartTime) || currentTime.isBefore(MorningStartTime)) && !hasSentNightMessage
    }

    private fun sendMorningMessage() {
        UnityPlayer.UnitySendMessage("SkyBlend", "ChangeDay", "")
        hasSentMorningMessage = true
    }

    private fun sendEveningMessage() {
        UnityPlayer.UnitySendMessage("SkyBlend", "ChangeEvening", "")
        hasSentEveningMessage = true
    }

    private fun sendNightMessage() {
        UnityPlayer.UnitySendMessage("SkyBlend", "ChangeNight", "")
        hasSentNightMessage = true
    }

    fun sendTimeBasedMessage(currentTime: LocalTime) {
        when {
            shouldSendMorningMessage(currentTime) -> sendMorningMessage()
            shouldSendEveningMessage(currentTime) -> sendEveningMessage()
            shouldSendNightMessage(currentTime) -> sendNightMessage()
        }
    }

    fun resetFlags() {
        hasSentMorningMessage = false
        hasSentEveningMessage = false
        hasSentNightMessage = false
    }

    companion object {
        private val MorningStartTime: LocalTime = LocalTime.of(6, 0)
        private val EveningStartTime: LocalTime = LocalTime.of(17, 0)
        private val NightStartTime: LocalTime = LocalTime.of(19, 0)
    }
}
