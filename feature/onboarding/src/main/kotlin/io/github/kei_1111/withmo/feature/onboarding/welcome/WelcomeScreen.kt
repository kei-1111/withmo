package io.github.kei_1111.withmo.feature.onboarding.welcome

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.onboarding.welcome.component.WelcomeScreenContent

@Suppress("ModifierMissing")
@Composable
fun WelcomeScreen(
    navigateSelectFavoriteApp: () -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val currentNavigateSelectFavoriteApp by rememberUpdatedState(navigateSelectFavoriteApp)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WelcomeEffect.NavigateSelectFavoriteApp -> currentNavigateSelectFavoriteApp()
            }
        }
    }

    WelcomeScreen(
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun WelcomeScreen(
    onAction: (WelcomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPaddingValue),
        ) {
            WelcomeScreenContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            WithmoButton(
                onClick = { onAction(WelcomeAction.OnNextButtonClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.Medium)
                    .height(CommonDimensions.SettingItemHeight),
            ) {
                BodyMediumText(
                    text = "次へ",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
@Preview
private fun WelcomeScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WelcomeScreen(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun WelcomeScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WelcomeScreen(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
