package com.example.withmo.ui.screens.notification_settings

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoSaveButton
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing")
@Composable
fun NotificationSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

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
        viewModel.onEvent(NotificationSettingsUiEvent.NavigateToSettingsScreen)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is NotificationSettingsUiEvent.ChangeIsNotificationAnimationEnabled -> {
                    if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)) {
                        viewModel.changeIsNotificationAnimationEnable(event.isNotificationAnimationEnabled)
                    } else {
                        viewModel.changeIsNotificationAnimationEnable(false)
                        viewModel.changeIsNotificationPermissionDialogShown(true)
                    }
                }

                is NotificationSettingsUiEvent.NotificationPermissionDialogOnConfirm -> {
                    launcher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                    viewModel.changeIsNotificationPermissionDialogShown(false)
                }

                is NotificationSettingsUiEvent.NotificationPermissionDialogOnDismiss -> {
                    viewModel.changeIsNotificationPermissionDialogShown(false)
                    showToast(context, "通知アニメーションを有効にするには\n通知アクセスを許可してください")
                }

                is NotificationSettingsUiEvent.Save -> {
                    viewModel.saveNotificationSettings()
                }

                is NotificationSettingsUiEvent.SaveSuccess -> {
                    showToast(context, "保存しました")
                    latestNavigateToSettingsScreen()
                }

                is NotificationSettingsUiEvent.SaveFailure -> {
                    showToast(context, "保存に失敗しました")
                }

                is NotificationSettingsUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    NotificationSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun NotificationSettingsScreen(
    uiState: NotificationSettingsUiState,
    onEvent: (NotificationSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()),
        ) {
            WithmoTopAppBar(
                content = { TitleLargeText(text = "通知") },
                navigateBack = { onEvent(NotificationSettingsUiEvent.NavigateToSettingsScreen) },
            )
            NotificationSettingsScreenContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(UiConfig.DefaultWeight)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { onEvent(NotificationSettingsUiEvent.Save) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiConfig.MediumPadding),
            )
        }
    }
    if (uiState.isNotificationPermissionDialogShown) {
        NotificationPermissionDialog(
            onConfirm = {
                onEvent(NotificationSettingsUiEvent.NotificationPermissionDialogOnConfirm)
            },
            onDismiss = {
                onEvent(NotificationSettingsUiEvent.NotificationPermissionDialogOnDismiss)
            },
        )
    }
}
