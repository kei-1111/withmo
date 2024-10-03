package com.example.withmo.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withmo.domain.model.AppInfo
import com.unity3d.player.UnityPlayer
import kotlinx.collections.immutable.ImmutableList

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
@Composable
fun App(
    unityPlayer: UnityPlayer?,
    appList: ImmutableList<AppInfo>,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState

    AppContent(
        uiState = uiState,
        unityPlayer = unityPlayer,
        navigateToSettingScreen = viewModel::navigateToSettingScreen,
        navigateToHomeScreen = viewModel::navigateToHomeScreen,
        appList = appList,
        modifier = Modifier.fillMaxSize(),
    )
}
