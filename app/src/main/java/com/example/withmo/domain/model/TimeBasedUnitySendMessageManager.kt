package com.example.withmo.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.utils.isEvening
import com.example.withmo.utils.isMorning
import com.example.withmo.utils.isNight
import com.unity3d.player.UnityPlayer
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class TimeBasedUnitySendMessageManager {

    private var hasSentMorningMessage = false
    private var hasSentEveningMessage = false
    private var hasSentNightMessage = false

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
}
