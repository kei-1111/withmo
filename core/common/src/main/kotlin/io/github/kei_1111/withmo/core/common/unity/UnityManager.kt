package io.github.kei_1111.withmo.core.common.unity

import android.content.Context
import com.unity3d.player.UnityPlayer

object UnityManager {
    private var _player: UnityPlayer? = null
    val player: UnityPlayer
        get() = _player ?: error("UnityPlayer not initialized")

    fun init(context: Context) {
        if (_player != null) return
        _player = UnityPlayer(context).apply {
            init(settings.getInt("gles_mode", 1), false)
        }
    }

    fun resume() = _player?.resume()
    fun pause() = _player?.pause()
    fun quit() = _player?.quit()
    fun focusGained(gained: Boolean) = _player?.windowFocusChanged(gained)
}
