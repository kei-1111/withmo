package io.github.kei_1111.withmo.feature.onboarding.screen.select_favorite_app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.designsystem.component.WithmoButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoOutlinedButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.onboarding.screen.select_favorite_app.component.SelectFavoriteAppScreenContent

@Suppress("ModifierMissing")
@Composable
internal fun SelectFavoriteAppScreen(
    navigateBack: () -> Unit,
    navigateSelectDisplayModel: () -> Unit,
    viewModel: SelectFavoriteAppViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val currentNavigateBack by rememberUpdatedState(navigateBack)
    val currentNavigateSelectDisplayModel by rememberUpdatedState(navigateSelectDisplayModel)

    BackHandler {
        viewModel.onAction(SelectFavoriteAppAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SelectFavoriteAppEffect.NavigateBack -> currentNavigateBack()
                is SelectFavoriteAppEffect.NavigateSelectDisplayModel -> currentNavigateSelectDisplayModel()
            }
        }
    }

    SelectFavoriteAppScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SelectFavoriteAppScreen(
    state: SelectFavoriteAppState,
    onAction: (SelectFavoriteAppAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    when (state) {
        SelectFavoriteAppState.Idle, SelectFavoriteAppState.Loading -> { /* TODO: デザインが決まっていないため */ }

        is SelectFavoriteAppState.Error -> { /* TODO: デザインが決まっていないため */ }

        is SelectFavoriteAppState.Stable -> {
            Surface(
                modifier = modifier,
                color = WithmoTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                ) {
                    WithmoTopAppBar {
                        Text(
                            text = "お気に入りアプリは？",
                            color = WithmoTheme.colorScheme.onSurface,
                            style = WithmoTheme.typography.titleLarge,
                        )
                    }
                    SelectFavoriteAppScreenContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        WithmoOutlinedButton(
                            onClick = { onAction(SelectFavoriteAppAction.OnBackButtonClick) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                        ) {
                            Text(
                                text = "戻る",
                                color = WithmoTheme.colorScheme.onSurface,
                                style = WithmoTheme.typography.bodyMedium,
                            )
                        }
                        WithmoButton(
                            onClick = { onAction(SelectFavoriteAppAction.OnNextButtonClick) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                        ) {
                            Text(
                                text = if (state.selectedAppList.isEmpty()) "スキップ" else "次へ",
                                style = WithmoTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun SelectFavoriteAppScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectFavoriteAppScreen(
            state = SelectFavoriteAppState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SelectFavoriteAppScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectFavoriteAppScreen(
            state = SelectFavoriteAppState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
