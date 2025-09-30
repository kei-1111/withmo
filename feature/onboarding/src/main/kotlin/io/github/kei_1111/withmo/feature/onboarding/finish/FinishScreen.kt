package io.github.kei_1111.withmo.feature.onboarding.finish

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
import io.github.kei_1111.withmo.core.designsystem.component.WithmoButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoOutlinedButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.onboarding.finish.component.FinishScreenContent

@Suppress("ModifierMissing")
@Composable
fun FinishScreen(
    onBackButtonClick: () -> Unit,
    navigateHome: () -> Unit,
    viewModel: FinishViewModel = hiltViewModel(),
) {
    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)
    val currentNavigateHome by rememberUpdatedState(navigateHome)

    BackHandler {
        viewModel.onAction(FinishAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FinishEffect.NavigateBack -> currentOnBackButtonClick()
                is FinishEffect.NavigateHome -> currentNavigateHome()
            }
        }
    }

    FinishScreen(
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun FinishScreen(
    onAction: (FinishAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    Surface(
        modifier = modifier,
        color = WithmoTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPaddingValue),
        ) {
            FinishScreenContent(
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
                    onClick = { onAction(FinishAction.OnBackButtonClick) },
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
                    onClick = { onAction(FinishAction.OnFinishButtonClick) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                ) {
                    Text(
                        text = "はじめる",
                        color = WithmoTheme.colorScheme.primary,
                        style = WithmoTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun FinishScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        FinishScreen(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun FinishScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        FinishScreen(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
