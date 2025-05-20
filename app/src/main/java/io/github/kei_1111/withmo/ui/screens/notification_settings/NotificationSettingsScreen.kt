package io.github.kei_1111.withmo.ui.screens.notification_settings

import android.content.Intent
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
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoSaveButton
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.notification_settings.component.NotificationPermissionDialog
import io.github.kei_1111.withmo.ui.screens.notification_settings.component.NotificationSettingsScreenContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing", "LongMethod")
@Composable
fun NotificationSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)) {
                viewModel.changeIsNotificationAnimationEnable(true)
            } else {
                viewModel.changeIsNotificationAnimationEnable(false)
            }
        },
    )

    BackHandler {
        viewModel.onAction(NotificationSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.action.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is NotificationSettingsAction.OnIsNotificationAnimationEnabledSwitchChange -> {
                    if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)) {
                        viewModel.changeIsNotificationAnimationEnable(event.isNotificationAnimationEnabled)
                    } else {
                        viewModel.changeIsNotificationAnimationEnable(false)
                        viewModel.changeIsNotificationPermissionDialogShown(true)
                    }
                }

                is NotificationSettingsAction.OnNotificationPermissionDialogConfirm -> {
                    launcher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                    viewModel.changeIsNotificationPermissionDialogShown(false)
                }

                is NotificationSettingsAction.OnNotificationPermissionDialogDismiss -> {
                    viewModel.changeIsNotificationPermissionDialogShown(false)
                    showToast(context, "通知アニメーションを有効にするには\n通知アクセスを許可してください")
                }

                is NotificationSettingsAction.OnSaveButtonClick -> {
                    viewModel.saveNotificationSettings(
                        onSaveSuccess = {
                            showToast(context, "保存しました")
                            currentOnBackButtonClick()
                        },
                        onSaveFailure = {
                            showToast(context, "保存に失敗しました")
                        },
                    )
                }

                is NotificationSettingsAction.OnBackButtonClick -> {
                    currentOnBackButtonClick()
                }
            }
        }.launchIn(this)
    }

    NotificationSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun NotificationSettingsScreen(
    uiState: NotificationSettingsUiState,
    onEvent: (NotificationSettingsAction) -> Unit,
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
                content = { TitleLargeText(text = "通知") },
                navigateBack = { onEvent(NotificationSettingsAction.OnBackButtonClick) },
            )
            NotificationSettingsScreenContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Weights.Medium)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { onEvent(NotificationSettingsAction.OnSaveButtonClick) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.Medium),
            )
        }
    }
    if (uiState.isNotificationPermissionDialogShown) {
        NotificationPermissionDialog(
            onConfirm = {
                onEvent(NotificationSettingsAction.OnNotificationPermissionDialogConfirm)
            },
            onDismiss = {
                onEvent(NotificationSettingsAction.OnNotificationPermissionDialogDismiss)
            },
        )
    }
}
