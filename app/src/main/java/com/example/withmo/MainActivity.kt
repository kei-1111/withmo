package com.example.withmo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AdaptiveIconDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.WithmoTheme
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.screens.home.HomeScreen
import com.example.withmo.ui.screens.setting.SettingScreen
import com.example.withmo.until.getAppList
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var unityPlayer: UnityPlayer? = null

    private var appList: List<AppInfo> by mutableStateOf(emptyList())

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "notification_received") {
                val packageName = intent.getStringExtra("notification_data")
                appList.map { app ->
                    if (app.packageName == packageName) app.receiveNotification()
                }
            } else {
                Log.d("", "onReceive: ${intent.action}")
                val newAppList = getAppList(this@MainActivity)
                Log.d("", "newAppListSize: ${newAppList.size}")
                newAppList.forEach { newApp ->
                    appList.find { it.packageName == newApp.packageName }?.let {
                        newApp.notification = it.notification
                        newApp.useCount = it.useCount
                    }
                }
                Log.d("", "newAppListSize: ${newAppList.size}")
                appList = newAppList
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        unityPlayer = UnityPlayer(this)

        appList = getAppList(this)

        registerReceiver(
            packageReceiver,
            IntentFilter("notification_received"),
            RECEIVER_NOT_EXPORTED
        )

        registerReceiver(
            packageReceiver,
            IntentFilter("start_activity"),
            RECEIVER_NOT_EXPORTED
        )

        registerReceiver(packageReceiver, IntentFilter().also {
            it.addAction(Intent.ACTION_PACKAGE_ADDED)
            it.addAction(Intent.ACTION_PACKAGE_REMOVED)
            it.addAction(Intent.ACTION_PACKAGE_CHANGED)
            it.addAction(Intent.ACTION_PACKAGE_REPLACED)
            it.addDataScheme("package")
        }, RECEIVER_NOT_EXPORTED)

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()

            WithmoTheme {
                HomeScreen(
                    unityPlayer = unityPlayer,
                    showSetting = { mainViewModel.setSettingState(true) },
                    appList = appList,
                )

                if (mainViewModel.getSettingState()) {
                    SettingScreen(
                        hideSetting = { mainViewModel.setSettingState(false) },
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

    override fun finish() {
    }
}


