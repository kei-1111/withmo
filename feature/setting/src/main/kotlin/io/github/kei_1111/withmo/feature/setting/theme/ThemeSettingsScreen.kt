package io.github.kei_1111.withmo.feature.setting.theme

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
import androidx.compose.material3.Surface
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
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSaveButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.feature.setting.theme.component.ThemeSettingsScreenContent

@Suppress("ModifierMissing")
@Composable
fun ThemeSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: ThemeSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    BackHandler {
        viewModel.onAction(ThemeSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ThemeSettingsEffect.NavigateBack -> currentOnBackButtonClick()

                is ThemeSettingsEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    ThemeSettingsSceen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ThemeSettingsSceen(
    state: ThemeSettingsState,
    onAction: (ThemeSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    when (state) {
        ThemeSettingsState.Idle, ThemeSettingsState.Loading -> { /* TODO: デザインが決まっていないため */ }

        is ThemeSettingsState.Error -> { /* TODO: デザインが決まっていないため */ }

        is ThemeSettingsState.Stable -> {
            Surface(
                modifier = modifier,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                ) {
                    WithmoTopAppBar(
                        content = { TitleLargeText(text = "テーマ") },
                        navigateBack = { onAction(ThemeSettingsAction.OnBackButtonClick) },
                    )
                    ThemeSettingsScreenContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                    )
                    WithmoSaveButton(
                        onClick = { onAction(ThemeSettingsAction.OnSaveButtonClick) },
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
private fun ThemeSettingsScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ThemeSettingsSceen(
            state = ThemeSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun ThemeSettingsScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ThemeSettingsSceen(
            state = ThemeSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
