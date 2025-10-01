package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.designsystem.component.WithmoButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoOutlinedButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.feature.onboarding.select_display_model.component.SelectDisplayModelScreenContent

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing", "LongMethod")
@Composable
fun SelectDisplayModelScreen(
    navigateBack: () -> Unit,
    navigateFinish: () -> Unit,
    viewModel: SelectDisplayModelViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        viewModel.onAction(SelectDisplayModelAction.OnOpenDocumentLauncherResult(uri))
    }

    val currentNavigateBack by rememberUpdatedState(navigateBack)
    val currentNavigateFinish by rememberUpdatedState(navigateFinish)

    BackHandler {
        viewModel.onAction(SelectDisplayModelAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SelectDisplayModelEffect.OpenDocument -> openDocumentLauncher.launch(arrayOf("*/*"))
                is SelectDisplayModelEffect.NavigateBack -> currentNavigateBack()
                is SelectDisplayModelEffect.NavigateFinish -> currentNavigateFinish()
                is SelectDisplayModelEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    SelectDisplayModelScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun SelectDisplayModelScreen(
    state: SelectDisplayModelState,
    onAction: (SelectDisplayModelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    when (state) {
        SelectDisplayModelState.Idle, SelectDisplayModelState.Loading -> { /* TODO: デザインが決まっていないため */ }

        is SelectDisplayModelState.Error -> { /* TODO: デザインが決まっていないため */ }

        is SelectDisplayModelState.Stable -> {
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
                            text = "表示したいVRMモデルは？",
                            color = WithmoTheme.colorScheme.onSurface,
                            style = WithmoTheme.typography.titleLarge,
                        )
                    }
                    SelectDisplayModelScreenContent(
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
                            onClick = { onAction(SelectDisplayModelAction.OnBackButtonClick) },
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
                            onClick = { onAction(SelectDisplayModelAction.OnNextButtonClick) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                        ) {
                            Text(
                                text = if (state.isDefaultModel) "スキップ" else "次へ",
                                style = WithmoTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SelectDisplayModelScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectDisplayModelScreen(
            state = SelectDisplayModelState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SelectDisplayModelScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectDisplayModelScreen(
            state = SelectDisplayModelState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
