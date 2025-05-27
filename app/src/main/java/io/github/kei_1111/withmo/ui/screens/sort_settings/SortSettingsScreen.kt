package io.github.kei_1111.withmo.ui.screens.sort_settings

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoSaveButton
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.sort_settings.component.SortSettingsScreenContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Suppress("ModifierMissing")
@Composable
fun SortSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: SortSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    BackHandler {
        viewModel.onAction(SortSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SortSettingsEffect.NavigateBack -> currentOnBackButtonClick()

                is SortSettingsEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    SortSettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SortSettingsScreen(
    state: SortSettingsState,
    onAction: (SortSettingsAction) -> Unit,
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
            WithmoTopAppBar(
                content = { TitleLargeText(text = "並び順") },
                navigateBack = { onAction(SortSettingsAction.OnBackButtonClick) },
            )
            SortSettingsScreenContent(
                state = state,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Weights.Medium)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { onAction(SortSettingsAction.OnSaveButtonClick) },
                enabled = state.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.Medium),
            )
        }
    }
}
