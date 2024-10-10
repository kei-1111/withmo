package com.example.withmo.ui.screens.theme_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.WithmoSaveButton
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.until.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing")
@Composable
fun ThemeSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: ThemeSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is ThemeSettingsUiEvent.ChangeThemeType -> {
                    viewModel.changeThemeType(event.themeType)
                }

                is ThemeSettingsUiEvent.Save -> {
                    viewModel.saveThemeSettings()
                }

                is ThemeSettingsUiEvent.SaveSuccess -> {
                    showToast(context, "保存しました")
                    latestNavigateToSettingsScreen()
                }

                is ThemeSettingsUiEvent.SaveFailure -> {
                    showToast(context, "保存に失敗しました")
                }

                is ThemeSettingsUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()),
        ) {
            WithmoTopAppBar(
                currentScreen = Screen.ThemeSettings,
                navigateBack = { viewModel.onEvent(ThemeSettingsUiEvent.NavigateToSettingsScreen) },
            )
            ThemeSettingsScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(UiConfig.DefaultWeight)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { viewModel.onEvent(ThemeSettingsUiEvent.Save) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiConfig.MediumPadding),
            )
        }
    }
}
