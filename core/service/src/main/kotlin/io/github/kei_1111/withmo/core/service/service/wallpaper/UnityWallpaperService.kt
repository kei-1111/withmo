package io.github.kei_1111.withmo.core.service.service.wallpaper

import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.Surface
import android.view.SurfaceHolder
import io.github.kei_1111.withmo.core.common.unity.UnityManager

class UnityWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = UnityEngine()

    private val mainHandler: Handler = Handler(Looper.getMainLooper())

    private inner class UnityEngine : Engine() {

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)
            UnityManager.init(this@UnityWallpaperService.applicationContext)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            holder.surface.let {
                mainHandler.postDelayed(
                    { startUnity(it) },
                    UNITY_SURFACE_READY_DELAY_MILLIS,
                )
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            stopUnity()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                startUnity(surfaceHolder.surface)
            } else {
                stopUnity()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            stopUnity()
        }
    }

    private fun startUnity(surface: Surface) {
        UnityManager.attachSurfaceForWallpaper(surface)
        UnityManager.resumeForWallpaper()
        UnityManager.focusGainedForWallpaper(true)
    }

    private fun stopUnity() {
        UnityManager.pauseForWallpaper()
        UnityManager.focusGainedForWallpaper(false)
        UnityManager.detachSurfaceForWallpaper()
    }

    private companion object {
        private const val UNITY_SURFACE_READY_DELAY_MILLIS = 100L
    }
}
