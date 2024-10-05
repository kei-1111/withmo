package com.example.withmo.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.AppInfo
import com.unity3d.player.UnityPlayer
import kotlinx.collections.immutable.ImmutableList

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
@Composable
fun App(
    unityPlayer: UnityPlayer?,
    appList: ImmutableList<AppInfo>,
) {
    AppContent(
        unityPlayer = unityPlayer,
        appList = appList,
        modifier = Modifier.fillMaxSize(),
    )
}
