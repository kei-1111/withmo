package io.github.kei_1111.withmo

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityManager
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.AppListProvider
import io.github.kei_1111.withmo.core.ui.AppWidgetHostsProvider
import io.github.kei_1111.withmo.core.ui.ClickBlockerProvider
import io.github.kei_1111.withmo.core.ui.CurrentTimeProvider
import io.github.kei_1111.withmo.core.ui.LocalCurrentTime
import io.github.kei_1111.withmo.core.util.FileUtils
import io.github.kei_1111.withmo.core.util.TimeUtils
import io.github.kei_1111.withmo.ui.App
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
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
    lateinit var appWidgetHost: AppWidgetHost

    @Inject
    lateinit var appWidgetManager: AppWidgetManager

    @Inject
    lateinit var modelFileManager: ModelFileManager

    @Inject
    lateinit var appManager: AppManager

    @Inject
    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var getSortSettingsUseCase: GetSortSettingsUseCase

    @Inject
    lateinit var saveSortSettingsUseCase: SaveSortSettingsUseCase

    @Inject
    lateinit var getNotificationSettingsUseCase: GetNotificationSettingsUseCase

    @Inject
    lateinit var saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase

    private val viewModel: MainViewModel by viewModels()

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            lifecycleScope.launch {
                appManager.refreshAppList()
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

        UnityManager.init(applicationContext)

        observeModelFilePath()
        observeThemeSettings()

        lifecycleScope.launchWhenCreated {
            appManager.refreshAppList()
        }

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
            val appInfoList by appManager.appInfoList.collectAsState()

            AppWidgetHostsProvider(
                appWidgetHost = appWidgetHost,
                appWidgetManager = appWidgetManager,
            ) {
                AppListProvider(
                    appList = appInfoList.toPersistentList(),
                ) {
                    CurrentTimeProvider {
                        val currentTime = LocalCurrentTime.current

                        LaunchedEffect(currentTime) {
                            if (themeSettings.themeType == ThemeType.TIME_BASED) {
                                TimeUtils.sendTimeBasedMessage(currentTime)
                            }
                        }

                        ClickBlockerProvider {
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
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        UnityManager.focusGainedForActivity(hasFocus)
    }

    override fun onResume() {
        super.onResume()
        UnityManager.resumeForActivity()

        // アプリがフォアグラウンドに戻った時に使用回数を更新
        lifecycleScope.launch {
            appManager.updateUsageCounts()

            // 権限チェックして設定を更新
            checkPermissionsAndUpdateSettings()
        }
    }

    override fun onPause() {
        super.onPause()
        UnityManager.pauseForActivity()
    }

    override fun onStop() {
        super.onStop()
        TimeUtils.resetFlags()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(packageReceiver)
    }

    private fun observeModelFilePath() {
        lifecycleScope.launch {
            val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath

            getModelFilePathUseCase().collect { modelFilePath ->
                val path = modelFilePath.path
                if (path != null) {
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

    private suspend fun checkPermissionsAndUpdateSettings() {
        val hasUsageStatsPermission = permissionChecker.isUsageStatsPermissionGranted()
        val hasNotificationListenerPermission = permissionChecker.isNotificationListenerEnabled()

        // UsageStats権限がない かつ 現在のソートが使用回数順の場合、アルファベット順に変更
        val currentSortSettings = getSortSettingsUseCase().first()
        if (!hasUsageStatsPermission && currentSortSettings.sortType == SortType.USE_COUNT) {
            saveSortSettingsUseCase(
                SortSettings(sortType = SortType.ALPHABETICAL),
            )
        }

        // 通知リスナー権限がない場合、通知関連設定をfalseに変更
        val currentNotificationSettings = getNotificationSettingsUseCase().first()
        if (!hasNotificationListenerPermission &&
            (
                currentNotificationSettings.isNotificationAnimationEnabled ||
                    currentNotificationSettings.isNotificationBadgeEnabled
                )
        ) {
            saveNotificationSettingsUseCase(
                NotificationSettings(
                    isNotificationAnimationEnabled = false,
                    isNotificationBadgeEnabled = false,
                ),
            )
        }
    }
}
