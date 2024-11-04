package com.example.withmo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.withmo.domain.model.TimeBasedMessageManager
import com.example.withmo.domain.model.user_settings.ThemeSettings
import com.example.withmo.domain.model.user_settings.ThemeType
import com.example.withmo.domain.repository.AppInfoRepository
import com.example.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import com.example.withmo.ui.App
import com.example.withmo.ui.theme.WithmoTheme
import com.example.withmo.utils.AppUtils
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var unityPlayer: UnityPlayer? = null

    @Inject
    lateinit var getThemeSettingsUseCase: GetThemeSettingsUseCase
    private var themeSettings by mutableStateOf(ThemeSettings())
    private var isNight by mutableStateOf(isNightTime(LocalTime.now(ZoneId.systemDefault())))
    private val timeBasedMessageManager = TimeBasedMessageManager()

    @Inject
    lateinit var appInfoRepository: AppInfoRepository

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val packageName = intent.getStringExtra("package_name")

            when (intent.action) {
                "notification_received" -> {
                    lifecycleScope.launch {
                        val currentAppInfo = packageName?.let { pkgName ->
                            appInfoRepository.getAppInfoByPackageName(pkgName)
                        } ?: return@launch

                        val updatedAppInfo = currentAppInfo.copy(notification = true)
                        appInfoRepository.updateAppInfo(updatedAppInfo)
                    }
                }
                "start_activity" -> {
                    lifecycleScope.launch {
                        val currentAppInfo = packageName?.let { pkgName ->
                            appInfoRepository.getAppInfoByPackageName(pkgName)
                        } ?: return@launch

                        val updatedAppInfo = currentAppInfo.copy(
                            notification = false,
                            useCount = currentAppInfo.useCount + 1,
                        )
                        appInfoRepository.updateAppInfo(updatedAppInfo)
                    }
                }
                else -> {
                    lifecycleScope.launch {
                        syncAppInfo()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Suppress("LongMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        unityPlayer = UnityPlayer(this)

        lifecycleScope.launchWhenCreated {
            syncAppInfo()
        }

        registerReceiver(
            packageReceiver,
            IntentFilter("notification_received"),
            RECEIVER_NOT_EXPORTED,
        )

        registerReceiver(
            packageReceiver,
            IntentFilter("start_activity"),
            RECEIVER_NOT_EXPORTED,
        )

        registerReceiver(
            packageReceiver,
            IntentFilter().also {
                it.addAction(Intent.ACTION_PACKAGE_ADDED)
                it.addAction(Intent.ACTION_PACKAGE_REMOVED)
                it.addAction(Intent.ACTION_PACKAGE_CHANGED)
                it.addAction(Intent.ACTION_PACKAGE_REPLACED)
                it.addDataScheme("package")
            },
            RECEIVER_NOT_EXPORTED,
        )

        lifecycleScope.launch {
            getThemeSettingsUseCase().collect { themeSettings = it }
        }

        setContent {
            LaunchedEffect(themeSettings) {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    while (isActive) {
                        val currentTime = LocalTime.now(ZoneId.systemDefault())
                        isNight = isNightTime(currentTime)

                        if (themeSettings.themeType == ThemeType.TIME_BASED) {
                            timeBasedMessageManager.sendTimeBasedMessage(currentTime)

                            if (currentTime.isAfter(NightStartTime)) {
                                timeBasedMessageManager.resetFlags()
                            }
                        }

                        delay(OneMinuteMillis)
                    }
                }
            }

            WithmoTheme(
                darkTheme = themeTypeToBoolean(themeSettings.themeType, isNight),
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isNavigationBarContrastEnforced = false
                }
                AlwaysShowNavigationBar()
                App(
                    unityPlayer = unityPlayer,
                )
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        unityPlayer?.windowFocusChanged(hasFocus)
    }

    override fun onResume() {
        super.onResume()
        unityPlayer?.resume()
    }

    override fun onPause() {
        super.onPause()
        unityPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        unityPlayer?.destroy()
        unregisterReceiver(packageReceiver)
    }

    @Suppress("EmptyFunctionBlock")
    override fun finish() {
    }

    private fun isNightTime(currentTime: LocalTime): Boolean {
        return currentTime.isAfter(NightStartTime) || currentTime.isBefore(MorningStartTime)
    }

    private fun themeTypeToBoolean(themeType: ThemeType, isNight: Boolean): Boolean {
        return when (themeType) {
            ThemeType.TIME_BASED -> {
                timeBasedMessageManager.resetFlags()
                isNight
            }
            ThemeType.LIGHT -> {
                UnityPlayer.UnitySendMessage("SkyBlend", "SetDayFixedMode", "")
                false
            }
            ThemeType.DARK -> {
                UnityPlayer.UnitySendMessage("SkyBlend", "SetNightFixedMode", "")
                true
            }
        }
    }

    private suspend fun syncAppInfo() {
        val installedApps = AppUtils.getAppList(this)

        appInfoRepository.syncWithInstalledApps(installedApps)
    }

    companion object {
        private const val OneMinuteMillis = 60_000L

        private val MorningStartTime: LocalTime = LocalTime.of(6, 0)
        private val NightStartTime: LocalTime = LocalTime.of(19, 0)
    }
}

@Composable
private fun AlwaysShowNavigationBar() {
    val view = LocalView.current

    DisposableEffect(view) {
        val insetsController = ViewCompat.getWindowInsetsController(view)
        insetsController?.apply {
            show(WindowInsetsCompat.Type.navigationBars()) // Show navigation bar
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }

        onDispose { /* Clean up if necessary */ }
    }
}
