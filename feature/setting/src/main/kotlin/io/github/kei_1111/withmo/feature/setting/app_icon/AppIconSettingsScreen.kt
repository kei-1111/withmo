package io.github.kei_1111.withmo.feature.setting.app_icon

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSaveButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.feature.setting.app_icon.component.AppIconSettingsScreenContent
import io.github.kei_1111.withmo.feature.setting.app_icon.component.AppItemPreviewArea

@Suppress("ModifierMissing")
@Composable
fun AppIconSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: AppIconSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    BackHandler {
        viewModel.onAction(AppIconSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AppIconSettingsEffect.NavigateBack -> currentOnBackButtonClick()

                is AppIconSettingsEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    AppIconSettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun AppIconSettingsScreen(
    state: AppIconSettingsState,
    onAction: (AppIconSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    when (state) {
        AppIconSettingsState.Idle, AppIconSettingsState.Loading -> { /* TODO: デザインが決まっていないため */ }

        is AppIconSettingsState.Error -> { /* TODO: デザインが決まっていないため */ }

        is AppIconSettingsState.Stable -> {
            Surface(
                modifier = modifier,
                color = WithmoTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                ) {
                    WithmoTopAppBar(
                        content = {
                            Text(
                                text = "アプリアイコン",
                                color = WithmoTheme.colorScheme.onSurface,
                                style = WithmoTheme.typography.titleLarge,
                            )
                        },
                        navigateBack = { onAction(AppIconSettingsAction.OnBackButtonClick) },
                    )
                    AppItemPreviewArea(
                        appIconSettings = state.appIconSettings,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    HorizontalDivider(Modifier.fillMaxWidth())
                    AppIconSettingsScreenContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                    )
                    WithmoSaveButton(
                        onClick = { onAction(AppIconSettingsAction.OnSaveButtonClick) },
                        enabled = state.isSaveButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun AppIconSettingsScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppIconSettingsScreen(
            state = AppIconSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun AppIconSettingsScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppIconSettingsScreen(
            state = AppIconSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
