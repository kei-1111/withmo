package io.github.kei_1111.withmo.core.common.unity

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Surface
import com.unity3d.player.UnityPlayer

/**
 * UnityManagerはUnityPlayerの管理を行うシングルトンオブジェクト。
 * ライブ壁紙機能の追加に伴い、Activity側で呼び出すメソッドとWallpaper側で呼び出すメソッドを分けた。
 * これにより、UnityPlayerのライフサイクルを適切に管理し、ActivityとWallpaperの両方でUnityPlayerを使用できるようにする。
 */
@Suppress("TooManyFunctions")
object UnityManager {
    private var _player: UnityPlayer? = null
    private val player: UnityPlayer
        get() = _player ?: error("UnityPlayer not initialized")

    private val mainHandler = Handler(Looper.getMainLooper())

    private const val MAIN_DISPLAY_INDEX = 0

    @Volatile
    private var currentUnitySurface: UnitySurface? = null

    @Synchronized
    fun init(context: Context) {
        if (_player != null) return
        _player = UnityPlayer(context).apply {
            init(settings.getInt("gles_mode", 1), false)
        }
    }

    fun resumeForActivity() = mainHandler.post {
        if (currentUnitySurface == UnitySurface.Activity) player.resume()
    }
    fun resumeForWallpaper() = mainHandler.post {
        if (currentUnitySurface == UnitySurface.Wallpaper) player.resume()
    }
    fun pauseForActivity() = mainHandler.post {
        if (currentUnitySurface == UnitySurface.Activity) player.pause()
    }
    fun pauseForWallpaper() = mainHandler.post {
        if (currentUnitySurface == UnitySurface.Wallpaper) player.pause()
    }
    fun focusGainedForActivity(gained: Boolean) = mainHandler.post {
        if (currentUnitySurface == UnitySurface.Activity) player.windowFocusChanged(gained)
    }
    fun focusGainedForWallpaper(gained: Boolean) = mainHandler.post {
        if (currentUnitySurface == UnitySurface.Wallpaper) player.windowFocusChanged(gained)
    }

    /**
     * Wallpaper用のSurfaceにアタッチするときは処理が重複しないように、現在のSurfaceがActivity（UnityView）のときのみアタッチする
     * アタッチの際には、デタッチしてからアタッチする。
     */
    fun attachSurfaceForWallpaper(surface: Surface) {
        if (currentUnitySurface == UnitySurface.Activity || currentUnitySurface == null) {
            // → Activity Surface だったものを detach
            player.displayChanged(MAIN_DISPLAY_INDEX, null)
            currentUnitySurface = UnitySurface.Wallpaper
            // Wallpaper Surface にアタッチ
            player.displayChanged(MAIN_DISPLAY_INDEX, surface)
        }
    }

    /**
     * Activity (UnityView)用のSurfaceにアタッチするときは処理が重複しないように、現在のSurfaceがWallpaperのときのみアタッチする
     * アタッチの際には、デタッチしてからアタッチする。
     */
    fun attachSurfaceForActivity(surface: Surface) {
        if (currentUnitySurface == UnitySurface.Wallpaper || currentUnitySurface == null) {
            // → Wallpaper Surface だったものを detach
            player.displayChanged(MAIN_DISPLAY_INDEX, null)
            currentUnitySurface = UnitySurface.Activity
            // Activity Surface にアタッチ
            player.displayChanged(MAIN_DISPLAY_INDEX, surface)
        }
    }

    /**
     * WallpaperにアタッチされているSurfaceを取り外すときのみ使う
     */
    fun detachSurfaceForWallpaper() {
        if (currentUnitySurface == UnitySurface.Wallpaper) {
            player.displayChanged(MAIN_DISPLAY_INDEX, null)
            currentUnitySurface = null // 壁紙モードをやめる
        }
    }

    /**
     * ActivityにアタッチされているSurfaceを取り外すときのみ使う
     */
    fun detachSurfaceForActivity() {
        if (currentUnitySurface == UnitySurface.Activity) {
            player.displayChanged(MAIN_DISPLAY_INDEX, null)
            currentUnitySurface = null // Activityモードをやめる
        }
    }
}

enum class UnitySurface {
    Wallpaper,
    Activity,
}
