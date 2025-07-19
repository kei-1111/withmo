package io.github.kei_1111.withmo.feature.setting.sort

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSaveButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.feature.setting.sort.component.SortSettingsScreenContent
import io.github.kei_1111.withmo.feature.setting.sort.component.UsagePermissionDialog

@Suppress("ModifierMissing")
@Composable
fun SortSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: SortSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    val usageStatsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        viewModel.onAction(SortSettingsAction.OnUsageStatsPermissionResult)
    }

    BackHandler {
        viewModel.onAction(SortSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SortSettingsEffect.NavigateBack -> currentOnBackButtonClick()

                is SortSettingsEffect.ShowToast -> showToast(context, effect.message)

                is SortSettingsEffect.RequestUsageStatsPermission -> {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    usageStatsPermissionLauncher.launch(intent)
                }
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

    if (state.isUsageStatsPermissionDialogVisible) {
        UsagePermissionDialog(
            onDismiss = { onAction(SortSettingsAction.OnUsageStatsPermissionDialogDismiss) },
            onConfirm = { onAction(SortSettingsAction.OnUsageStatsPermissionDialogConfirm) },
        )
    }
}

@Composable
@Preview
private fun SortSettingsScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SortSettingsScreen(
            state = SortSettingsState(
                sortSettings = SortSettings(
                    sortType = SortType.ALPHABETICAL,
                ),
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SortSettingsScreenDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SortSettingsScreen(
            state = SortSettingsState(
                sortSettings = SortSettings(
                    sortType = SortType.USE_COUNT,
                ),
                isSaveButtonEnabled = false,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
