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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.user_settings.ThemeSettings
import com.example.withmo.domain.model.user_settings.ThemeType
import com.example.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import com.example.withmo.ui.App
import com.example.withmo.ui.theme.WithmoTheme
import com.example.withmo.until.getAppList
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var unityPlayer: UnityPlayer? = null

    private var appList: List<AppInfo> by mutableStateOf(emptyList())

    private val packageReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "notification_received") {
                val packageName = intent.getStringExtra("notification_data")
                appList.map { app ->
                    if (app.packageName == packageName) app.receiveNotification()
                }
            } else {
                val newAppList = getAppList(this@MainActivity)

                newAppList.forEach { newApp ->
                    appList.find { it.packageName == newApp.packageName }?.let {
                        newApp.notification = it.notification
                        newApp.useCount = it.useCount
                    }
                }
                appList = newAppList
            }
        }
    }

    @Inject
    lateinit var getThemeSettingsUseCase: GetThemeSettingsUseCase
    private var themeSettings by mutableStateOf(ThemeSettings())

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        unityPlayer = UnityPlayer(this)

        appList = getAppList(this)

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
            var isNight by remember { mutableStateOf(isNightTime()) }

            LaunchedEffect(Unit) {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    while (true) {
                        isNight = isNightTime()
                        delay(MillisecondToSecond * SecondToMinute)
                    }
                }
            }

            WithmoTheme(
                darkTheme = themeTypeToBoolean(themeSettings.themeType, isNight),
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isNavigationBarContrastEnforced = false
                }
                App(
                    unityPlayer = unityPlayer,
                    appList = appList.toPersistentList(),
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("MagicNumber")
    private fun isNightTime(): Boolean {
        val currentTime = LocalTime.now()
        val startNightTime = LocalTime.of(19, 0)
        val endNightTime = LocalTime.of(6, 0)

        return currentTime.isAfter(startNightTime) || currentTime.isBefore(endNightTime)
    }

    private fun themeTypeToBoolean(themeType: ThemeType, isNight: Boolean): Boolean {
        return when (themeType) {
            ThemeType.TIME_BASED -> isNight
            ThemeType.LIGHT -> false
            ThemeType.DARK -> true
        }
    }

    companion object {
        private const val MillisecondToSecond = 1000L
        private const val SecondToMinute = 60L
    }
}
