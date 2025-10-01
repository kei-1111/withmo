package io.github.kei_1111.withmo.feature.setting.screens.sort

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
import io.github.kei_1111.withmo.feature.setting.screens.sort.component.SortSettingsScreenContent
import io.github.kei_1111.withmo.feature.setting.screens.sort.component.UsagePermissionDialog

@Suppress("ModifierMissing")
@Composable
fun SortSettingsScreen(
    navigateBack: () -> Unit,
    viewModel: SortSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val currentNavigateBack by rememberUpdatedState(navigateBack)

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
                is SortSettingsEffect.NavigateBack -> currentNavigateBack()

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

    when (state) {
        SortSettingsState.Idle, SortSettingsState.Loading -> { /* TODO: デザインが決まっていないため */ }

        is SortSettingsState.Error -> { /* TODO: デザインが決まっていないため */ }

        is SortSettingsState.Stable -> {
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
                                text = "並び順",
                                color = WithmoTheme.colorScheme.onSurface,
                                style = WithmoTheme.typography.titleLarge,
                            )
                        },
                        navigateBack = { onAction(SortSettingsAction.OnBackButtonClick) },
                    )
                    SortSettingsScreenContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                    )
                    WithmoSaveButton(
                        onClick = { onAction(SortSettingsAction.OnSaveButtonClick) },
                        enabled = state.isSaveButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
    }
}

@Composable
@Preview
private fun SortSettingsScreenLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SortSettingsScreen(
            state = SortSettingsState.Stable(),
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
            state = SortSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
