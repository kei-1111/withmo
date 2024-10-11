package com.example.withmo.ui.screens.display_model_setting

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.utils.FileUtils
import com.example.withmo.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.Q)
@Suppress("ModifierMissing")
@Composable
fun DisplayModelSettingScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: DisplayModelSettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    BackHandler {
        viewModel.onEvent(DisplayModelSettingUiEvent.NavigateToSettingsScreen)
    }

    LaunchedEffect(Unit) {
        val modelFileList = FileUtils.getModelFile(context)
        viewModel.getModelFileList(modelFileList)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is DisplayModelSettingUiEvent.SelectModelFile -> {
                    viewModel.selectModelFile(event.modelFile)
                    event.modelFile.sendPathToUnity()
                    showToast(context, "モデルを選択しました")
                    latestNavigateToSettingsScreen()
                }

                is DisplayModelSettingUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    DisplayModelSettingScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun DisplayModelSettingScreen(
    uiState: DisplayModelSettingUiState,
    onEvent: (DisplayModelSettingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()),
        ) {
            WithmoTopAppBar(
                currentScreen = Screen.DisplayModelSetting,
                navigateBack = { onEvent(DisplayModelSettingUiEvent.NavigateToSettingsScreen) },
            )
            DisplayModelSettingScreenContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
