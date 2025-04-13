package io.github.kei_1111.withmo

import android.appwidget.AppWidgetHost
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
import androidx.lifecycle.lifecycleScope
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.AndroidEntryPoint
import io.github.kei_1111.withmo.domain.model.TimeBasedUnitySendMessageManager
import io.github.kei_1111.withmo.domain.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.domain.model.user_settings.ThemeType
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.GetModelFilePathUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.ui.App
import io.github.kei_1111.withmo.ui.composition.CurrentTimeProvider
import io.github.kei_1111.withmo.ui.composition.LocalCurrentTime
import io.github.kei_1111.withmo.ui.theme.WithmoTheme
import io.github.kei_1111.withmo.utils.AppUtils
import io.github.kei_1111.withmo.utils.FileUtils
import io.github.kei_1111.withmo.utils.isEvening
import io.github.kei_1111.withmo.utils.isMorning
import io.github.kei_1111.withmo.utils.isNight
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var unityPlayer: UnityPlayer? = null

    @Inject
    lateinit var getThemeSettingsUseCase: GetThemeSettingsUseCase
    private var themeSettings by mutableStateOf(ThemeSettings())
    private val timeBasedUnitySendMessageManager = TimeBasedUnitySendMessageManager()

    @Inject
    lateinit var getModelFilePathUseCase: GetModelFilePathUseCase

    @Inject
    lateinit var appInfoRepository: AppInfoRepository

    @Inject
    lateinit var appWidgetHost: AppWidgetHost

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

        getDisplayModelSetting(this)

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
            CurrentTimeProvider {
                val currentTime = LocalCurrentTime.current

                UnitySendThemeMessage(themeSettings.themeType)

                LaunchedEffect(currentTime) {
                    if (themeSettings.themeType == ThemeType.TIME_BASED) {
                        timeBasedUnitySendMessageManager.sendTimeBasedMessage(currentTime)
                    }
                }

                WithmoTheme(
                    themeType = themeSettings.themeType,
                ) {
                    App(
                        unityPlayer = unityPlayer,
                    )
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        unityPlayer?.windowFocusChanged(hasFocus)
    }

    override fun onResume() {
        super.onResume()
        unityPlayer?.onResume()
    }

    override fun onPause() {
        super.onPause()
        unityPlayer?.onPause()
    }

    override fun onStop() {
        super.onStop()
        appWidgetHost.stopListening()
        timeBasedUnitySendMessageManager.resetFlags()
    }

    override fun onDestroy() {
        super.onDestroy()
        unityPlayer?.destroy()
        unregisterReceiver(packageReceiver)
    }

    @Suppress("EmptyFunctionBlock")
    override fun finish() {
    }

    private suspend fun syncAppInfo() {
        val installedApps = AppUtils.getAppList(this)

        appInfoRepository.syncWithInstalledApps(installedApps)
    }

    private fun getDisplayModelSetting(context: Context) {
        lifecycleScope.launch {
            val defaultModelFilePath = FileUtils.copyVrmFileFromAssets(context)?.absolutePath

            getModelFilePathUseCase().collect { modelFilePath ->
                if (modelFilePath.path != null) {
                    val path = modelFilePath.path
                    if (FileUtils.fileExists(path)) {
                        UnityPlayer.UnitySendMessage("VRMloader", "LoadVRM", path)
                    } else {
                        UnityPlayer.UnitySendMessage("VRMloader", "LoadVRM", defaultModelFilePath)
                    }
                } else {
                    UnityPlayer.UnitySendMessage("VRMloader", "LoadVRM", defaultModelFilePath)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun UnitySendThemeMessage(themeType: ThemeType) {
    val currentTime = LocalCurrentTime.current.toLocalTime()

    LaunchedEffect(themeType) {
        when (themeType) {
            ThemeType.TIME_BASED -> {
                when {
                    isMorning(currentTime) -> {
                        UnityPlayer.UnitySendMessage("SkyBlend", "ChangeDay", "")
                    }
                    isEvening(currentTime) -> {
                        UnityPlayer.UnitySendMessage("SkyBlend", "ChangeEvening", "")
                    }
                    isNight(currentTime) -> {
                        UnityPlayer.UnitySendMessage("SkyBlend", "ChangeNight", "")
                    }
                }
            }
            ThemeType.LIGHT -> {
                UnityPlayer.UnitySendMessage("SkyBlend", "SetDayFixedMode", "")
            }
            ThemeType.DARK -> {
                UnityPlayer.UnitySendMessage("SkyBlend", "SetNightFixedMode", "")
            }
        }
    }
}
