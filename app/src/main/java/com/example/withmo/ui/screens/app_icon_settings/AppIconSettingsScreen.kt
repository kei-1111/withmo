package com.example.withmo.ui.screens.app_icon_settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoSaveButton
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing")
@Composable
fun AppIconSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: AppIconSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    BackHandler {
        viewModel.onEvent(AppIconSettingsUiEvent.NavigateToSettingsScreen)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is AppIconSettingsUiEvent.ChangeAppIconSize -> {
                    viewModel.changeAppIconSize(event.appIconSize)
                }

                is AppIconSettingsUiEvent.ChangeAppIconShape -> {
                    viewModel.changeAppIconShape(event.appIconShape)
                }

                is AppIconSettingsUiEvent.ChangeRoundedCornerPercent -> {
                    viewModel.changeRoundedCornerPercent(event.roundedCornerPercent)
                }

                is AppIconSettingsUiEvent.ChangeIsAppNameShown -> {
                    viewModel.changeIsAppNameShown(event.isAppNameShown)
                }

                is AppIconSettingsUiEvent.Save -> {
                    viewModel.saveAppIconSettings()
                }

                is AppIconSettingsUiEvent.SaveSuccess -> {
                    showToast(context, "保存しました")
                    latestNavigateToSettingsScreen()
                }

                is AppIconSettingsUiEvent.SaveFailure -> {
                    showToast(context, "保存に失敗しました")
                }

                is AppIconSettingsUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    AppIconSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun AppIconSettingsScreen(
    uiState: AppIconSettingsUiState,
    onEvent: (AppIconSettingsUiEvent) -> Unit,
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
                content = { TitleLargeText(text = "アプリアイコン") },
                navigateBack = { onEvent(AppIconSettingsUiEvent.NavigateToSettingsScreen) },
            )
            AppItemPreviewArea(
                appIconSettings = uiState.appIconSettings,
                modifier = Modifier.fillMaxWidth(),
            )
            HorizontalDivider(Modifier.fillMaxWidth())
            AppIconSettingsScreenContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(UiConfig.DefaultWeight)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { onEvent(AppIconSettingsUiEvent.Save) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiConfig.MediumPadding),
            )
        }
    }
}
