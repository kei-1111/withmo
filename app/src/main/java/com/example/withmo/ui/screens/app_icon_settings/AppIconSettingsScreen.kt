package com.example.withmo.ui.screens.app_icon_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.WithmoSaveButton
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.until.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing", "LongMethod")
@Composable
fun AppIconSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: AppIconSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

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

                is AppIconSettingsUiEvent.ChangeAppIconHorizontalSpacing -> {
                    viewModel.changeAppIconHorizontalSpacing(event.appIconHorizontalSpacing)
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

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            WithmoTopAppBar(
                currentScreen = Screen.AppIconSettings,
                navigateBack = navigateToSettingsScreen,
            )
            AppItemPreviewArea(
                appIconSettings = uiState.appIconSettings,
                modifier = Modifier.fillMaxWidth(),
            )
            HorizontalDivider(Modifier.fillMaxWidth())
            AppIconSettingsScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(UiConfig.DefaultWeight)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { viewModel.onEvent(AppIconSettingsUiEvent.Save) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiConfig.MediumPadding),
            )
        }
    }
}
