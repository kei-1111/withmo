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
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.kei_1111.withmo.common.AndroidToUnityMessenger
import io.github.kei_1111.withmo.common.UnityMethod
import io.github.kei_1111.withmo.common.UnityObject
import io.github.kei_1111.withmo.domain.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.domain.model.user_settings.ThemeType
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.GetModelFilePathUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.ui.App
import io.github.kei_1111.withmo.ui.UnityManager
import io.github.kei_1111.withmo.ui.composition.CurrentTimeProvider
import io.github.kei_1111.withmo.ui.composition.LocalCurrentTime
import io.github.kei_1111.withmo.ui.theme.WithmoTheme
import io.github.kei_1111.withmo.utils.AppUtils
import io.github.kei_1111.withmo.utils.FileUtils
import io.github.kei_1111.withmo.utils.TimeUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getThemeSettingsUseCase: GetThemeSettingsUseCase
    private var themeSettings by mutableStateOf(ThemeSettings())

    @Inject
    lateinit var getModelFilePathUseCase: GetModelFilePathUseCase

    @Inject
    lateinit var appInfoRepository: AppInfoRepository

    @Inject
    lateinit var appWidgetHost: AppWidgetHost

    private val viewModel: MainViewModel by viewModels()

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
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.startScreen == null
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        UnityManager.init(this)

        getDisplayModelSetting(this)
        observeThemeSettings()

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

        setContent {
            CurrentTimeProvider {
                val currentTime = LocalCurrentTime.current

                LaunchedEffect(currentTime) {
                    if (themeSettings.themeType == ThemeType.TIME_BASED) {
                        TimeUtils.sendTimeBasedMessage(currentTime)
                    }
                }

                WithmoTheme(
                    themeType = themeSettings.themeType,
                ) {
                    viewModel.startScreen?.let {
                        App(
                            startScreen = it,
                        )
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        UnityManager.focusGained(hasFocus)
    }

    override fun onResume() {
        super.onResume()
        UnityManager.resume()
    }

    override fun onPause() {
        super.onPause()
        UnityManager.pause()
    }

    override fun onStop() {
        super.onStop()
        appWidgetHost.stopListening()
        TimeUtils.resetFlags()
    }

    override fun onDestroy() {
        super.onDestroy()
        UnityManager.quit()
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
                        AndroidToUnityMessenger.sendMessage(UnityObject.VRMloader, UnityMethod.LoadVRM, path)
                    } else {
                        defaultModelFilePath?.let {
                            AndroidToUnityMessenger.sendMessage(UnityObject.VRMloader, UnityMethod.LoadVRM, defaultModelFilePath)
                        }
                    }
                }
            }
        }
    }

    private fun observeThemeSettings() {
        lifecycleScope.launch {
            getThemeSettingsUseCase().collect {
                themeSettings = it
                TimeUtils.themeMessage(themeSettings.themeType)
            }
        }
    }
}
